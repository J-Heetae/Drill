from typing import Union
import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

import os
from dotenv import load_dotenv
import boto3 # S3 연결

import sys
from .addcomponents.addmodel import check_model


app = FastAPI()

origins = [
    "http://localhost:8000/*",
    "http://k9a106a.p.ssafy.io:*"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins = origins,
    allow_credentials = True, # cross-origin request에서 cookies를 포함할 것인지
    allow_methods = ["GET", "POST"], # cross-origin request로 허용할 method들
    allow_headers = ["*"], # cross-origin request로 허용할 HTTP Header의 목록 / Accept, Accept-Language, Content-Language, Content-Type은 CORS에서 항상 허용되는 헤더, 추가적인 사항에 대하여 적으면 된다.
)

load_dotenv() # .env 파일에 있는 키와 값을 환경변수로 등록해주는 library

'''
S3 연결
'''
client_s3 = boto3.client(
    's3',
    aws_access_key_id = os.environ.get("CREDENTIALS_ACCESS_KEY"),
    aws_secret_access_key = os.environ.get("CREDENTIALS_SECRET_KEY")
)


@app.get("/")
def read_root():
    check = check_model()
    return {"Hello": "jenkinsWorld",
            "check": check}

@app.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}

@app.get("/a106/{name}/{status}")
def read_name(name: str, status: str):
    return {"상태 :" : f"A106 팀의 {name}이(가) {status} 입니다."}

@app.get("/download/video/{objectname}/{filename}")
def amazon_s3(objectname: str, filename: str):
    # client_s3.download_file(os.environ.get("S3_BUCKET"), "Video/20231017_191311.mp4", "Video/testvideo.mp4")
    with open(f'video/{filename}.mp4', 'wb') as f:
        client_s3.download_fileobj(os.environ.get("S3_BUCKET"), f"Video/{objectname}.mp4", f)
    return {"status" : "success 200"}

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