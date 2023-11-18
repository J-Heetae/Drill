from typing import Union
import uvicorn
from fastapi import FastAPI, Query
from fastapi.responses import HTMLResponse, JSONResponse
from fastapi.middleware.cors import CORSMiddleware

import os
from dotenv import load_dotenv
import boto3 # S3 연결

import sys
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

@app.get("/information", response_class=HTMLResponse)
async def read_root():
    with open(os.path.join(static_path, "info.html"), "r", encoding="utf-8") as file:
        html_content = file.read()
    return HTMLResponse(content=html_content)

@app.get("/video/download/{filename}")
async def amazon_s3(filename: str):
    now_path = docker_container_path_check() # docker container 내부 path
    video_path = f"video/{filename}.mp4"
    file_path = os.path.join(now_path, video_path) # 저장할 파일명 + 확장자 mp4
    bucket_name = os.environ.get("S3_BUCKET")
    with open(file_path, 'wb') as fi:
        try:
            client_s3.download_fileobj(bucket_name, f"Video/{filename}.mp4", fi)
        except Exception as e:
            return JSONResponse(content= {"download": False, "check": "영상 다운로드 불가", "status" : 404})
    if os.path.exists(file_path):
        content = {"download": True, "status" : 200}
    else:
        content = {"download": False, "check": "영상이 폴더에 없습니다.", "status" : 400}
    return JSONResponse(content = content)

@app.delete("/video/remove/{filename}")
def remove_video(filename: str):
    now_path = docker_container_path_check()
    video_path = os.path.join(now_path, f"video/{filename}.mp4")
    thumbnails_path = os.path.join("/app", f"thumbnails/{filename}.jpg")
    if os.path.exists(video_path):
        os.remove(video_path)
        content = {"remove" : True, "status": 200}
    else:
        content = {"remove" : False, "status": 404}
    if os.path.exists(thumbnails_path):
        os.remove(thumbnails_path)
    else:
        print("썸네일 없어요")
    return JSONResponse(content = content)

@app.get("/video/process/{filename}")
def process_video(filename: str, hold_color: str = Query("파랑", alias="hold_color")): # docker container에 저장된 동영상 파일 cv2로 실행되는 지 확인
    print("hold_color", hold_color)
    result = ut.video_process(filename, hold_color)
    # if not result:
    #     ut.remove_video(filename)
    content = {"result" : result}
    headers = {"Content-Language": "en-US"}
    return JSONResponse(content = content, headers = headers)

 
if __name__ == "__main__":
    uvicorn.run(app = "__main__:app", host="0.0.0.0", port = 8000, reload = True)
