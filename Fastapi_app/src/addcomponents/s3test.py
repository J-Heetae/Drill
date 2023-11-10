# import boto3
# from dotenv import load_dotenv
# import os

# load_dotenv() # use environment variables

# client_s3 = boto3.client(
#     's3',
#     aws_access_key_id = os.environ.get("CREDENTIALS_ACCESS_KEY"),
#     aws_secret_access_key = os.environ.get("CREDENTIALS_SECRET_KEY")
# )

# res = client_s3.delete_objects(Bucket = os.environ.get("S3_BUCKET"),
#                               Delete = {
#                                   'Objects': [
#                                       {
#                                           'Key': 'Thumbnail/test1.jpg'
#                                       }
#                                   ],
#                                   'Quiet': False,
#                               })

# print(res)

import cv2
img = cv2.imread("974.jpg")
img = cv2.resize(img, (450, 450))
x, y = 200, 400
print(img[y, x])
# 이미지를 그레이스케일로 변환
# gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

# 이미지의 밝기 값을 조정
# adjusted = cv2.equalizeHist(img)

# 이미지의 잡음 제거
denoised = cv2.fastNlMeansDenoisingColored(img,None,10,10,7,21)

print(denoised[y, x])

cv2.imshow('img', img)
cv2.imshow('denoised', denoised)
cv2.waitKey(0)