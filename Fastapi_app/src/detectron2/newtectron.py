from detectron2.config import get_cfg
from detectron2.engine.defaults import DefaultPredictor
from detectron2.utils.visualizer import Visualizer
from detectron2.data.catalog import MetadataCatalog
import os
import cv2
import matplotlib.pyplot as plt
import numpy as np
import copy
import colorsys

MODEL_DIRECTORY = os.getcwd() + "/model"
# MODEL_DIRECTORY = os.getcwd() + "\\" + "detectron2\\" + "model\\"

# Get config and weigths for model
cfg = get_cfg()
cfg.merge_from_file(os.path.join(MODEL_DIRECTORY, "experiment_config.yml"))
cfg.MODEL.WEIGHTS = os.path.join(MODEL_DIRECTORY, "model_final.pth")
cfg.MODEL.DEVICE='cpu'

# Set metadata, in this case only the class names for plotting
MetadataCatalog.get("meta").thing_classes = ["hold", "volume"]
metadata = MetadataCatalog.get("meta")

predictor = DefaultPredictor(cfg)


def bgr_to_hsv(bgr):
  bgr_np = np.array([[[bgr[0], bgr[1], bgr[2]]]], dtype= np.uint8)
  bgr_hsv = cv2.cvtColor(bgr_np, cv2.COLOR_BGR2HSV).tolist()
  return bgr_hsv[0][0]

hsv_palette = {
  "빨강" : bgr_to_hsv([83, 75, 249]),
  "검정" : bgr_to_hsv([21, 22, 20]),
  "노랑" : bgr_to_hsv([91, 226, 255]),
  "보라" : bgr_to_hsv([159, 68, 100]),
  "하양" : bgr_to_hsv([217, 225, 224]),
  "초록" : bgr_to_hsv([66, 107, 68]),
  "핑크" : bgr_to_hsv([158, 81, 216]),
  "파랑" : bgr_to_hsv([167, 64, 1]),
  "주황" : bgr_to_hsv([1, 124, 231]),
}

def get_hold_info(img, hold_color):
  outputs = predictor(img)
  
  # 이미지 HSV로 변환
  hsv_img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
  
  box_info = []
  masks = outputs["instances"].pred_masks.tolist()
  for i in range(len(outputs["instances"].pred_boxes)):
    h = 0
    s = 0
    v = 0
    hsv_cnt = 0
    mask = masks[i]
    x_length = len(mask)
    y_length = len(mask[0])
    
    # 마스크에서 HSV 값 추출
    for x in range(x_length):
      for y in range(y_length):
        if mask[x][y]:
          hsv_color = hsv_img[x, y]
          h += hsv_color[0]
          s += hsv_color[1]
          v += hsv_color[2]
          hsv_cnt += 1
    
    coordinates = outputs["instances"].pred_boxes[i].tensor.tolist()[0]
    # 좌표 추출
    top_left_x = coordinates[0]
    top_left_y = coordinates[1]
    bottom_right_x = coordinates[2]
    bottom_right_y = coordinates[3]
    # center_x = int((top_left_x + bottom_right_x) / 2)
    # center_y = int((top_left_y + bottom_right_y) / 2)
    # height = bottom_right_y - top_left_y
    # width = bottom_right_x - top_left_x
    
    # 종류 추출
    category = "volume" if outputs["instances"].pred_classes[i] else "hold"
    
    # 평균 HSV값 추출
    hsv_color = (int(h/hsv_cnt), int(s/hsv_cnt), int(v/hsv_cnt))
    
    # 중앙값을 RGB로 추출 -> HSV로 변환
    # center_bgr = img[center_x, center_y]
    # hsv_color = bgr_to_hsv([int(center_bgr[2]), int(center_bgr[1]), int(center_bgr[0])]) 
    

    # H 0.6, S 0.2, V 0.2 가중치로 유사도 계산
    # for key, value in hsv_palette.items():
    #   hsv_dist = 0
    #   hsv_dist += abs(hsv_color[0] - value[0]) * 0.6 + abs(hsv_color[1] - value[1]) * 0.2 + abs(hsv_color[2] - value[2]) * 0.2
    #   # print(i, "번째 ", key, "dist : ", hsv_dist)
    #   if hsv_dist < dist:
    #     predicted_color = key
    #     dist = hsv_dist
    # print(i, predicted_color)
    
    # # 유클리드 거리로 가장 가까운 색 찾기
    # for key, value in bgr_palette.items():
    #   euc_dist = 0
    #   for i in range(3):
    #     euc_dist += (hsv_color[i] - value[i]) ** 2
    #   if euc_dist < dist:
    #     predicted_color = key
    #     dist = euc_dist


    # 색깔 별로 거리 비교해서 가장 가까운 것 찾기
    predicted_color = ""
    dist = 9999999 
    # H 0.6, S 0.2, V 0.2 가중치로 유사도 계산
    for key, value in hsv_palette.items():
      hsv_dist = 0
      hsv_dist += abs(hsv_color[0] - value[0]) * 0.6 + abs(hsv_color[1] - value[1]) * 0.2 + abs(hsv_color[2] - value[2]) * 0.2
      # print(i, "번째 ", key, "dist : ", hsv_dist)
      if hsv_dist < dist:
        predicted_color = key
        dist = hsv_dist
    # print(i, predicted_color)
    
    # H 0.6, S 0.2, V 0.2 가중치로 유사도 계산
    selected_color = hsv_palette[hold_color]
    hsv_dist = 0
    hsv_dist += abs(hsv_color[0] - selected_color[0]) * 0.6 + abs(hsv_color[1] - selected_color[1]) * 0.2 + abs(hsv_color[2] - selected_color[2]) * 0.2
    # 45보다 작으면 같은 계열 색이라 판단하고 return 값에 추가함
    if hsv_dist < 45:  
      # [홀드/볼륨, 좌상단 x, 좌상단 y, 우하단 x, 우하단 y, hsv값, 예측된 색깔] return 
      box_info.append([category, top_left_x, top_left_y, bottom_right_x, bottom_right_y, hsv_color, predicted_color])
    
  # 이미지에 추출한 rgb 그려서 출력
  img2 = copy.deepcopy(hsv_img)
  img3 = copy.deepcopy(hsv_img) 
  for box in box_info:
    cv2.rectangle(img=img2, pt1=(int(box[1]), int(box[2])), pt2=(int(box[3]), int(box[4])), color=box[5], thickness=-1)
    cv2.rectangle(img=img3, pt1=(int(box[1]), int(box[2])), pt2=(int(box[3]), int(box[4])), color=hsv_palette[box[6]], thickness=-1)
  for i in range(len(box_info)):
    cv2.rectangle(img=img2, pt1=(int(box_info[i][1]), int(box_info[i][2])), pt2=(int(box_info[i][3]), int(box_info[i][4])), color=box_info[i][5], thickness=-1)
    cv2.rectangle(img=img3, pt1=(int(box_info[i][1]), int(box_info[i][2])), pt2=(int(box_info[i][3]), int(box_info[i][4])), color=hsv_palette[box_info[i][6]], thickness=-1)
    cv2.putText(img=img2, text=str(i), org=(int(box_info[i][1]), int(box_info[i][2])), fontFace=cv2.FONT_ITALIC, fontScale=0.5, color=(255, 0, 0), thickness=2, lineType=cv2.LINE_AA)  
    cv2.putText(img=img3, text=str(i), org=(int(box_info[i][1]), int(box_info[i][2])), fontFace=cv2.FONT_ITALIC, fontScale=0.5, color=(255, 0, 0), thickness=2, lineType=cv2.LINE_AA)
  fig, (ax1, ax2, ax3, ax4) = plt.subplots(1, 4)
  ax1.imshow(img[:, :, ::-1])
  ax1.axis('off')
  ax1.set_title('Original')
  ax1.legend()
  
  ax2.imshow(hsv_img[:, :, ::-1])
  ax2.axis('off')
  ax2.set_title('HSV Converted')

  ax3.imshow(img2[:, :, ::-1])
  ax3.axis('off')
  ax3.set_title('Detected holds')
  
  
  ax4.imshow(img3[:, :, ::-1])
  ax4.axis('off')
  ax4.set_title('Categorized holds')

  fig.tight_layout()
  plt.show()
  
  # y값 순으로 정렬
  box_info.sort(key = lambda x : (x[2]))
  
  print(box_info[0])
  # 제일 y 값이 작은 (맨 위에 있는) 홀드 return
  return box_info
