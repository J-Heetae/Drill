import numpy as np
import cv2
import os
import mediapipe as mp
import json

def get_params(request): # Function to get params
    if 'video_name' not in request.args:
        return "None"
    video_name = request.args.get('video_name')
    return video_name


def make_thumbnail_folder(): # Function to make thumbnail folder
    try:
        folderpath = os.getcwd() + "/thumbnails"
        if not os.path.exists(folderpath):
            os.makedirs(folderpath)
    except OSError:
        print ('Error: Creating directory. ' +  folderpath)
    return folderpath


def thumbnail_extraction(image, video_path): # Function to extract thumbnail image
    filepath = make_thumbnail_folder()
    imgname = video_path.split("/")[-1].split(".")[0] + ".jpg"
    filepath = filepath + "/" + imgname
    print(filepath)
    cv2.imwrite(filepath, image)
    print('썸네일 추출 성공')
    return imgname

def thumbnail_upload(folder_path: str, filename: str): # Function to upload thumbnail image to S3
    import boto3
    from dotenv import load_dotenv
    import os

    load_dotenv() # use environment variables

    client_s3 = boto3.client(
        's3',
        aws_access_key_id = os.environ.get("CREDENTIALS_ACCESS_KEY"),
        aws_secret_access_key = os.environ.get("CREDENTIALS_SECRET_KEY")
    )

    try:
        client_s3.upload_file(
            folder_path + "/" + filename,
            os.environ.get("S3_BUCKET"),
            "Thumbnail/" + filename,
            ExtraArgs = {'ContentType' : 'img/jpg'}
        )
        return True
    except Exception as e:
        print(f"Another error => {e}")
    return False

def remove_video(video_name): # Function to delete video file in S3
    import boto3
    from dotenv import load_dotenv
    import os

    load_dotenv() # use environment variables

    client_s3 = boto3.client(
        's3',
        aws_access_key_id = os.environ.get("CREDENTIALS_ACCESS_KEY"),
        aws_secret_access_key = os.environ.get("CREDENTIALS_SECRET_KEY")
    )

    res = client_s3.delete_objects(Bucket = os.environ.get("S3_BUCKET"),
                                Delete = {
                                    'Objects': [
                                        {
                                            'Key': 'Video/' + video_name + '.mp4',
                                        }
                                    ],
                                    'Quiet': False,
                                })

    return res

def compare_location(wrist_positions, hold_positions):
    print('--------------------------------------')
    print(wrist_positions)
    print(hold_positions)
    print('--------------------------------------')
    lx, ly, rx, ry = hold_positions
    wrist_y, wrist_x = wrist_positions
    if ly < wrist_y < ry and lx < wrist_x < rx:
        return True
    return False

def video_process(video_name): # Function to extract the location of user's wrist from a video file
    import cv2
    import os
    import mediapipe as mp
    import heapq
    video_name += '.mp4'
    mp_pose = mp.solutions.pose
    video_path = os.getcwd() + "/video/" + video_name

    cap = cv2.VideoCapture(video_path)
    fps = cap.get(cv2.CAP_PROP_FPS) // 3
    cnt, frame_num = 0, 0
    wrist_all = []
    
    with mp_pose.Pose(
        static_image_mode=True,
        min_detection_confidence=0.3,
        min_tracking_confidence=0.4,
        model_complexity = 2) as pose:
        while True:
            success, image = cap.read()
            
            if not success:
                print("동영상을 찾을 수 없습니다.")
                break
            
            frame_num += 1
            if frame_num % fps: # 프레임 처리할 개수 설정
                continue
            
            image = cv2.resize(image, (450, 450))
            
            if not cnt:
                folderpath = make_thumbnail_folder() # thumbnails folder 만드는 함수
                imgname = thumbnail_extraction(image, video_path) # thumbnail 추출해서 폴더에 넣어주는 함수
                check_upload = thumbnail_upload(folderpath, imgname)
                print('들어갑니다. detectron2')
                hold_top_value = hold_extraction(image) # 홀드 인식 / list로 반환
                cnt += 1
            
            if check_upload:
                return False
            
            image.flags.writeable = False
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            results = pose.process(image)
            h, w, c = image.shape
            image.flags.writeable = True
            
            for i in range(15, 17): # wrist label : 15, 16
                flag = 0
                if results.pose_landmarks:
                    if results.pose_landmarks.landmark[mp_pose.PoseLandmark(i).value]:
                        position_wrist = results.pose_landmarks.landmark[mp_pose.PoseLandmark(i).value]
                    else:
                        flag = 1
                        continue
                    if not flag:
                        dx, dy = position_wrist.x * w, position_wrist.y * h
                        heapq.heappush(wrist_all, (dy, dx))
    # print('------------------------------------------------')
    # print(heapq.heappop(wrist_all))
    return compare_location(heapq.heappop(wrist_all), hold_top_value) # return wrist's y value



def hold_extraction(image): # Function to extract hold in image using detectron2
    # print(os.getcwd())
    newpath = os.getcwd() + '\\detectron2'
    os.chdir(newpath)
    print(os.getcwd())
    import newtectron as dt
    # output : [hold/volume, 좌측상단x, 좌측상단y, 우측하단x, 우측하단y, (b, g, r), 유사색]]
    results = dt.get_hold_info(image)
    # print(results)
    # print(os.getcwd())
    newpath = os.getcwd()[:-11]
    # print(newpath)
    os.chdir(newpath)
    # print(os.getcwd())
    return [20, 20, 20, 20]

def color_classification(img_path): # Function to classify color in image
    img_color = cv2.imread(img_path) # 이미지 파일을 컬러로 불러옴
    img_color = cv2.resize(img_color, (450, 450))
    cv2.imshow('img', img_color)
    height, width = img_color.shape[:2] # 이미지의 높이와 너비 불러옴, 가로 [0], 세로[1]

    # color = np.array([[[69,186,223]]], dtype=np.uint8) # 노란색
    # color = np.array([[[226,231,227]]], dtype=np.uint8) # 흰색
    color = np.array([[[216,81,158]]], dtype=np.uint8) # 핑크색
    color = cv2.cvtColor(color, cv2.COLOR_BGR2HSV)

    img_hsv = cv2.cvtColor(img_color, cv2.COLOR_BGR2HSV) # cvtColor 함수를 이용하여 hsv 색공간으로 변환

    # 핑크색
    # lower_blue = (187-40, 20, 20) # hsv 이미지에서 바이너리 이미지로 생성 , 적당한 값 30
    # upper_blue = (187, 255, 255)
    color = color[0][0]
    # print(color)
    # print(type(color))

    # lower_blue = (20, 140, 170) # hsv 이미지에서 바이너리 이미지로 생성 , 적당한 값 30
    # upper_blue = (80, 255, 255)
    lower_blue, upper_blue = np.empty((0, 3)), np.empty((0, 3))
    # print(lower_blue)
    # print(upper_blue)
    for i in color:
        # print(i)
        if i - 50 < 0:
            lower_blue = np.append(lower_blue, [0])
            upper_blue = np.append(upper_blue, [i + 50])
        elif i + 50 > 255:
            lower_blue = np.append(lower_blue, [i - 50])
            upper_blue = np.append(upper_blue, [255])
        else:
            lower_blue = np.append(lower_blue, [i - 50])
            upper_blue = np.append(upper_blue, [i + 50])
        # print(lower_blue)
        # print(upper_blue)

    img_mask = cv2.inRange(img_hsv, lower_blue, upper_blue) # 범위내의 픽셀들은 흰색, 나머지 검은색

    # 흰색 픽셀의 좌표를 추출
    white_pixel_coords = np.argwhere(img_mask == 255)
    print(len(white_pixel_coords))
    # white_pixel_coords에는 (행, 열) 형식의 좌표가 들어있음

    # for coord in white_pixel_coords:
    #     x, y = coord[1], coord[0]  # x와 y 좌표를 추출
    #     print(f"White Pixel Coordinate: x={x}, y={y}")

    img_result = cv2.bitwise_and(img_color, img_color, mask = img_mask)
    # 바이너리 이미지를 마스크로 사용하여 원본이미지에서 범위값에 해당하는 영상부분을 획득


    cv2.imshow('img_color', img_color)
    cv2.imshow('img_mask', img_mask)
    cv2.imshow('img_color', img_result)

    cv2.waitKey(0)
