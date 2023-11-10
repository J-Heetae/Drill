from detectron2.config import get_cfg
from detectron2.engine.defaults import DefaultPredictor
from detectron2.utils.visualizer import Visualizer
from detectron2.data.catalog import MetadataCatalog
import os
import cv2
import matplotlib.pyplot as plt
import numpy as np
import copy

MODEL_DIRECTORY = os.getcwd() + "\\" + "model\\"
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

bgr_palette = {
  "빨강" : (83, 75, 249),
  "검정" : (21, 22, 20),
  "노랑" : (6, 208, 227),
  "보라" : (139, 68, 88),
  "하양" : (224, 239, 237),
  "초록" : (139, 170, 57),
  "핑크" : (11, 52, 227),
  "파랑" : (167, 64, 1),
  "주황" : (1, 124, 231),
  "민트" : (200, 181, 93),
  "연두" : (25, 187, 142)
}

def get_hold_info(img):
  outputs = predictor(img)

  # out_predictions, predicted_color = v.draw_instance_predictions(outputs["instances"].to("cpu"))
  # img_holds = out_predictions.get_image()
  # img_colors = predicted_color
  # print(img_colors)
  # # display the results
  # fig, (ax1, ax2) = plt.subplots(1, 2)
  # ax1.imshow(img[:, :, ::-1])
  # ax1.axis('off')
  # ax1.set_title('Original')

  # ax2.imshow(img_holds)
  # ax2.axis('off')
  # ax2.set_title('Detected holds')

  # fig.tight_layout()
  # plt.show()

  # get coordinates
  # tensor(좌측 상단 x 좌표, 좌측 상단 y 좌표, 우측 하단 x좌표, 우측 하단 y 좌표)
  # [hold/volume, 좌측상단x, 좌측상단y, 우측하단x, 우측하단y, (r, g, b)]] 형태로 저장
  

  
  box_info = []
  masks = outputs["instances"].pred_masks.tolist()
  for i in range(len(outputs["instances"].pred_boxes)):
    red = 0
    green = 0
    blue = 0
    rgb_cnt = 0
    mask = masks[i]
    x_length = len(mask)
    y_length = len(mask[0])
    for x in range(x_length):
      for y in range(y_length):
        if mask[x][y]:
          bgr_color = img[x, y]
          red += bgr_color[2]
          green += bgr_color[1]
          blue += bgr_color[0]
          rgb_cnt += 1
    coordinates = outputs["instances"].pred_boxes[i].tensor.tolist()[0]
    # 좌표 추출
    top_left_x = coordinates[0]
    top_left_y = coordinates[1]
    bottom_right_x = coordinates[2]
    bottom_right_y = coordinates[3]
    # center_x = (top_left_x + bottom_right_x) / 2
    # center_y = (top_left_y + bottom_right_y) / 2
    # height = bottom_right_y - top_left_y
    # width = bottom_right_x - top_left_x
    
    # 종류 추출
    category = "volume" if outputs["instances"].pred_classes[i] else "hold"
    
    # RGB 추출
    bgr_color = (int(blue/rgb_cnt), int(green/rgb_cnt), int(red/rgb_cnt))
    
    predicted_color = ""
    dist = 9999999
    
    # 유클리드 거리로 가장 가까운 색 찾기
    for key, value in bgr_palette.items():
      euc_dist = 0
      for i in range(3):
        euc_dist += (bgr_color[i] - value[i]) ** 2
      if euc_dist < dist:
        predicted_color = key

    box_info.append([category, top_left_x, top_left_y, bottom_right_x, bottom_right_y, bgr_color, predicted_color])

  # 이미지에 추출한 rgb 그려서 출력
  img2 = copy.deepcopy(img)
  for box in box_info:
    cv2.rectangle(img=img2, pt1=(int(box[1]), int(box[2])), pt2=(int(box[3]), int(box[4])), color=box[5], thickness=-1)
    print(box[6])
  
  fig, (ax1, ax2) = plt.subplots(1, 2)
  ax1.imshow(img[:, :, ::-1])
  ax1.axis('off')
  ax1.set_title('Original')

  ax2.imshow(img2[:, :, ::-1])
  ax2.axis('off')
  ax2.set_title('Detected holds')

  fig.tight_layout()
  plt.show()
  
  return box_info
  
# SAMPLE_IMAGE = "974.jpg"
# get_hold_info(SAMPLE_IMAGE)
