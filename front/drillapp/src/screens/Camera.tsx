import React from 'react';
import { Alert, Button } from 'react-native';
import { launchCamera, CameraOptions } from 'react-native-image-picker';
import { CameraRoll } from '@react-native-camera-roll/camera-roll';
import Styled from 'styled-components/native';

const Camera = () => {

  
  const showPicker = () => {
    Alert.alert(
      '',
      '카메라로 촬영해서 업로드해보세요.',
      [
        {
          text: '카메라로 촬영',
          onPress: () => {
            launchCamera({ mediaType: 'video', saveToPhotos: true }, handleResponse);
          },
        },
      ],
      { cancelable: true }
    );
  };

  const handleResponse = (response: any) => {
    if (response.didCancel) {
      console.log('취소');
    } else if (response.errorCode) {
      console.error(response.errorCode);
    } else if (response.assets && response.assets.length > 0) {
      const videoURI = response.assets[0].uri;
    } else {
      console.error('비디오 선택되지 않았습니다.');
    }
  };

  return (
    <Container>
      <Button title="카메라 열기" onPress={showPicker} />
    </Container>
  );
};

const Container = Styled.View`
  flex: 1;
  align-items: center;
  justify-content: center;
`;

export default Camera;