import numpy as np
import cv2
import mediapipe as mp

img_color = cv2.imread('974.jpg') # 이미지 파일을 컬러로 불러옴
img_color = cv2.resize(img_color, (450, 450))
cv2.imshow('img', img_color)
height, width = img_color.shape[:2] # 이미지의 높이와 너비 불러옴, 가로 [0], 세로[1]

color = np.array([[[69,186,223]]], dtype=np.uint8)
color = cv2.cvtColor(color, cv2.COLOR_BGR2HSV);

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
    if i - 70 < 0:
        lower_blue = np.append(lower_blue, [0])
        upper_blue = np.append(upper_blue, [i + 70])
    elif i + 70 > 255:
        lower_blue = np.append(lower_blue, [i - 70])
        upper_blue = np.append(upper_blue, [255])
    else:
        lower_blue = np.append(lower_blue, [i - 70])
        upper_blue = np.append(upper_blue, [i + 70])
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


mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles
mp_hands = mp.solutions.hands
mp_pose = mp.solutions.pose

video_path = "../video/test2.mp4"
cap = cv2.VideoCapture(video_path)
# with mp_hands.Hands(
#     model_complexity=0,
#     max_num_hands=2,
#     min_detection_confidence=0.5,
#     min_tracking_confidence=0.5) as hands:
#     while True:
#         status, image = cap.read()
#         # print(status, image)
#         if not status:
#             print("동영상을 찾을 수 없습니다.")
#             # 동영상을 불러올 경우는 'continue' 대신 'break'를 사용합니다.
#             break
#         image = cv2.resize(image, (450, 450))
#         # 필요에 따라 성능 향상을 위해 이미지 작성을 불가능함으로 기본 설정합니다.
#         image.flags.writeable = False
#         image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
#         results = hands.process(image)

#         # 이미지에 손 주석을 그립니다.
#         image.flags.writeable = True
#         image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
#         if results.multi_hand_landmarks:
#             for hand_landmarks in results.multi_hand_landmarks:
#                 for id, lm in enumerate(hand_landmarks.landmark):
#                     if id != 9:
#                         continue
#                     h, w, c = image.shape
#                     dx, dy = int(lm.x*w), int(lm.y*h)
#                     print("{} : {} {}".format(id, dx, dy))
#                 mp_drawing.draw_landmarks(
#                     image,
#                     hand_landmarks,
#                     # mp_hands.HAND_CONNECTIONS,
#                     # mp_drawing_styles.get_default_hand_landmarks_style(),
#                     # mp_drawing_styles.get_default_hand_connections_style()
#                     )
#         #보기 편하게 이미지를 좌우 반전합니다.
#         # cv2.imshow('MediaPipe Hands', image)
#         cv2.imshow('MediaPipe Hands', cv2.flip(image, 1))
#         if cv2.waitKey(5) & 0xFF == ord('q'):
#             break
# cap.release()


cap = cv2.VideoCapture(video_path)
with mp_pose.Pose(
        min_detection_confidence=0.5,
        min_tracking_confidence=0.5) as pose:
    while cap.isOpened():
        success, image = cap.read()
        image = cv2.resize(image, (450, 450))
        if not success:
            print("동영상을 찾을 수 없습니다.")
            # 동영상을 불러올 경우는 'continue' 대신 'break'를 사용합니다.
            break

        # 필요에 따라 성능 향상을 위해 이미지 작성을 불가능함으로 기본 설정합니다.
        image.flags.writeable = False
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        results = pose.process(image)

        # 포즈 주석을 이미지 위에 그립니다.
        image.flags.writeable = True
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        mp_drawing.draw_landmarks(
            image,
            results.pose_landmarks,
            mp_pose.POSE_CONNECTIONS,
            landmark_drawing_spec=mp_drawing_styles.get_default_pose_landmarks_style())
        # 보기 편하게 이미지를 좌우 반전합니다.
        cv2.imshow('MediaPipe Pose', cv2.flip(image, 1))
        if cv2.waitKey(5) & 0xFF == ord('q'):
            break
cap.release()