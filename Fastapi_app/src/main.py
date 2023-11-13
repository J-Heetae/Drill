from typing import Union
import uvicorn
from fastapi import FastAPI
from fastapi.responses import HTMLResponse, StreamingResponse, JSONResponse
from fastapi.middleware.cors import CORSMiddleware

import os
from dotenv import load_dotenv
import boto3 # S3 연결
import subprocess
import cv2

import sys
# from .addcomponents.addmodel import check_model
from .addcomponents import utilities as ut

app = FastAPI()

origins = [
    "http://localhost:8000/*",
    "http://k9a106a.p.ssafy.io*"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins = origins,
    allow_credentials = True, # cross-origin request에서 cookies를 포함할 것인지
    allow_methods = ["GET", "POST"], # cross-origin request로 허용할 method들
    allow_headers = ["*"], # cross-origin request로 허용할 HTTP Header의 목록 / Accept, Accept-Language, Content-Language, Content-Type은 CORS에서 항상 허용되는 헤더, 추가적인 사항에 대하여 적으면 된다.
)

static_path = os.path.join(os.path.dirname(os.path.realpath(__file__)), "static")

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
sys.path.append(os.getcwd() + '/detectron2')

load_dotenv() # .env 파일에 있는 키와 값을 환경변수로 등록해주는 library

'''
S3 연결
'''
client_s3 = boto3.client(
    's3',
    aws_access_key_id = os.environ.get("CREDENTIALS_ACCESS_KEY"),
    aws_secret_access_key = os.environ.get("CREDENTIALS_SECRET_KEY")
)


def docker_container_path_check(): # docker container 내부 path
    return os.path.dirname(os.path.realpath(__file__))

@app.get("/")
async def read_root():
    return {"0" : "fastapi 1번째 8000 포트 다시 체크"}

@app.get("/information", response_class=HTMLResponse)
async def read_root():
    with open(os.path.join(static_path, "info.html"), "r", encoding="utf-8") as file:
        html_content = file.read()
    return HTMLResponse(content=html_content)

@app.get("/a106/{name}/{status}")
def read_name(name: str, status: str):
    return {"상태 :" : f"A106 팀의 {name}이(가) {status} 입니다."}

@app.get("/video/download/{filename}")
def amazon_s3(filename: str):
    # filename = ut.get_params(request)
    print(filename)
    now_path = docker_container_path_check() # docker container 내부 path
    print(now_path)
    video_path = f"video/{filename}.mp4"
    file_path = os.path.join(now_path, video_path) # 저장할 파일명 + 확장자 mp4
    print(file_path)
    bucket_name = os.environ.get("S3_BUCKET")
    print(bucket_name)
    with open(file_path, 'wb') as fi:
        try:
            client_s3.download_fileobj(bucket_name, f"Video/{filename}.mp4", fi)
        except:
            return JSONResponse(content= {"download": False,"status" : 404})
    if os.path.exists(file_path):
        content = {"download": True, "status" : 200}
    else:
        content = {"download": False, "status" : 400}
    return JSONResponse(content = content)

@app.get("/video/remove/{filename}")
def remove_video(filename: str):
    # filename = ut.get_params(request)
    print(filename)
    now_path = docker_container_path_check()
    print(now_path)
    file_path = now_path + f"\\video\\{filename}.mp4"
    print(file_path)
    if os.path.isfile(file_path):
        print("파일 있어요. 제거합니다.")
        os.remove(file_path)
        content = {"remove" : True, "status": 200}
    else:
        print("파일 없어요")
        content = {"remove" : False, "status": 404}
    return JSONResponse(content = content)

@app.get("/video/process/{filename}")
def process_video(filename: str): # docker container에 저장된 동영상 파일 cv2로 실행되는 지 확인
    # filename = ut.get_params(request) # get filename in request
    # now_path = docker_container_path_check() # get current path
    # file_path = os.path.join(now_path, f"{filename}.mp4")
    result = ut.video_process(filename)
    # if not result:
    #     ut.remove_video(filename)
    content = {"result" : result}
    headers = {"Content-Language": "en-US"}
    return JSONResponse(content = content, headers = headers)




    

# # 비디오 캡처 객체 생성
# video = cv2.VideoCapture(file_path)

# async def generate_frames():
#     while video.isOpened():
#         ret, frame = video.read()
#         if not ret:
#             break
#         # 프레임 처리 (예: 비디오에 어떤 가공을 한 후 이미지 반환)
#         # 여기에서 프레임을 가공한 후 반환할 수 있습니다.
#         ret, buffer = cv2.imencode('.jpg', frame)
#         if ret:
#             frame = buffer.tobytes()
#             yield (b'--frame\r\n'
#                    b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
# return StreamingResponse(generate_frames(), media_type='multipart/x-mixed-replace; boundary=frame')

@app.get("api/videopath/download")
async def videopath(video_ids : str):
    '''
    # 파일 직접 다운로드
    client_s3.download_file('BUCKET_NAME', 'OBJECT_NAME', 'FILE_NAME')
    for video in video_path:
        name = video.split(".")
        client_s3.download_file(os.getenv("S3_BUCKET"), name[0] + "_s3" + name[1] ,"video")

    # 파일 객체에 Write하여 다운로드
    with open('FILE_NAME', 'wb') as f:
        client_s3.download_fileobj('BUCKET_NAME', 'OBJECT_NAME', f)
    '''
    
    '''
    # 불러온 video file을 model에 넣어서 결과값 가져오는 함수
    
    '''
    pass




@app.get("api/img/upload")
async def imguploadtoaws(img_ids: str):
    pass
    '''
    imgpath(os)를 받으면 S3에 저장하는 코드
    모델을 돌린 후 os에 저장한 파일을 다시 S3에 올리는 작업이 필요함
    S3에 업로드하는 방법
    -   s3.upload_file(
            '업로드할 파일 경로', 
            '버킷이름', 
            '저장할 파일 명칭', 
            ExtraArgs={'ACL':'public-read'} contenttype 지정해줘야 에러 안뜸
        )
    
    imgURL = [] # S3에 저장한 음성파일 URL
    try:
        for img in img_ids:
            client_s3.upload_file(
                "./img/" + img,
                os.getenv("S3_BUCKET"),
                "img/" + img,
                ExtraArgs = {'ContentType' : 'img/jpg'}
            )
            imgURL.append(os.getenv("S3_URL") + "/" + img)
        
        return imgURL
    
    except ClientError as e:
        print(f'Credential error => {e}')
    except Exception as e:
        print(f"Another error => {e}")  
    '''

 
if __name__ == "__main__":
    uvicorn.run(app = "__main__:app", host="0.0.0.0", port = 8000, reload = True)
