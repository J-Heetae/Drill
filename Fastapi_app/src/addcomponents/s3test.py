import cv2
import mediapipe as mp

mp_drawing = mp.solutions.drawing_utils
mp_pose = mp.solutions.pose
video_path = "../video/test13.mp4"

cap = cv2.VideoCapture(video_path)
fps = cap.get(cv2.CAP_PROP_FPS) // 3
frame_num = 0

with mp_pose.Pose(
    static_image_mode=True,
    min_detection_confidence=0.3,
    min_tracking_confidence=0.4,
    model_complexity=2) as pose:

    while True:
        success, image = cap.read()

        if not success:
            print("동영상을 찾을 수 없습니다.")
            break

        frame_num += 1
        if frame_num % fps:  # 프레임 처리할 개수 설정
            continue

        image = cv2.resize(image, (450, 450))
        image.flags.writeable = False
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        results = pose.process(image)
        image.flags.writeable = True
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        if results.pose_landmarks is not None:
            landmarks = results.pose_landmarks.landmark
            # 랜드마크 15번과 16번 좌표를 얻어옴
            landmark_15 = (int(landmarks[mp_pose.PoseLandmark.LEFT_WRIST].x * image.shape[1]),
                           int(landmarks[mp_pose.PoseLandmark.LEFT_WRIST].y * image.shape[0]))
            landmark_16 = (int(landmarks[mp_pose.PoseLandmark.RIGHT_WRIST].x * image.shape[1]),
                           int(landmarks[mp_pose.PoseLandmark.RIGHT_WRIST].y * image.shape[0]))

            # 15번과 16번 랜드마크만 점으로 그리기
            cv2.circle(image, landmark_15, 5, (0, 255, 0), -1)
            cv2.circle(image, landmark_16, 5, (0, 255, 0), -1)

        cv2.imshow('MediaPipe Pose', cv2.flip(image, 1))
        if cv2.waitKey(5) & 0xFF == ord("q"):
            break

cap.release()
cv2.destroyAllWindows()
