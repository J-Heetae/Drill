from detectron2.config import get_cfg
from detectron2.engine.defaults import DefaultPredictor
from detectron2.utils.visualizer import Visualizer
from detectron2.data.catalog import MetadataCatalog
import os
import cv2
import matplotlib.pyplot as plt



def get_hold_info(image):
  print(os.getcwd())
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

  img = cv2.imread(image)
  outputs = predictor(img)
  v = Visualizer(
      img[:, :, ::-1],
      metadata=metadata
  )

  out_predictions, predicted_color = v.draw_instance_predictions(outputs["instances"].to("cpu"))
  img_holds = out_predictions.get_image()
  img_colors = predicted_color
  # print(img_colors)
  # display the results
  fig, (ax1, ax2) = plt.subplots(1, 2)
  ax1.imshow(img[:, :, ::-1])
  ax1.axis('off')
  ax1.set_title('Original')

  ax2.imshow(img_holds)
  ax2.axis('off')
  ax2.set_title('Detected holds')

  fig.tight_layout()
  plt.show()

  # get coordinates
  # tensor(좌측 상단 x 좌표, 좌측 상단 y 좌표, 우측 하단 x좌표, 우측 하단 y 좌표)
  # [hold/volume, center_x, center-y, height, width, (r, g, b)] 형태로 저장
  box_info = []
  for i in range(len(outputs["instances"].pred_boxes)):
    print(outputs["instances"].pred_boxes[i].tensor.tolist()[0])
    coordinates = outputs["instances"].pred_boxes[i].tensor.tolist()[0]
    # 좌표 추출
    top_left_x = coordinates[0]
    top_left_y = coordinates[1]
    bottom_right_x = coordinates[2]
    bottom_right_y = coordinates[3]
    center_x = (top_left_x + bottom_right_x) / 2
    center_y = (top_left_y + bottom_right_y) / 2
    height = bottom_right_y - top_left_y
    width = bottom_right_x - top_left_x
    
    # 종류 추출
    category = "volume" if outputs["instances"].pred_classes[i] else "hold"
    
    # RGB 추출
    rgb_color = (int(predicted_color[i][0] * 255), int(predicted_color[i][1] * 255), int(predicted_color[i][2] * 255))
    

    box_info.append([category, center_x, center_y, height, width, rgb_color])

  print(box_info)
  return box_info
  
SAMPLE_IMAGE = "974.jpg"
get_hold_info(SAMPLE_IMAGE)
