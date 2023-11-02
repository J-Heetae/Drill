import React from 'react';
import { Alert, Button } from 'react-native';
import {launchCamera, launchImageLibrary} from 'react-native-image-picker';
import { ImagePickerResponse } from 'react-native-image-picker';
import AWS from 'aws-sdk';
import Styled from 'styled-components/native';



AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});


const s3 = new AWS.S3();

const Camera = () => {
  const showPicker = () => {
    Alert.alert(
      '선택하세요',
      '카메라로 촬영하거나 앨범에서 선택하세요',
      [
        {
          text: '카메라로 촬영',
          onPress: () => {
            launchCamera({ mediaType: 'video' }, handleResponse); 
          },
        },
        {
          text: '앨범에서 선택',
          onPress: () => {
            launchImageLibrary({ mediaType: 'video' }, handleResponse); 
          },
        },
      ],
      { cancelable: true }
    );
  };

  const handleResponse = async (response: ImagePickerResponse) => {
    if (response.didCancel) {
      console.log('취소');
    } else if (response.errorCode) {
      console.error(response.errorCode);
    } else if (response.assets && response.assets.length > 0) {
      const uri = response.assets[0].uri;
      const fileName = response.assets[0].fileName;
      console.log('파일이름---------------------------', fileName);
      console.log('URI---------------------------', uri);
      uploadVideoToS3(uri, fileName);
    } else {
      console.error('이미지가 선택되지 않았습니다.');
    }
  };

  const uploadVideoToS3 = async (uri: string | undefined, fileName: string | undefined) => {
    if (uri && fileName) {  
      try {
        const response = await fetch(uri);
        const blob = await response.blob();
  
        const params = {
          Bucket: 'drill-video-bucket', 
          Key: `Video/${fileName}`,
          Body: blob,
          ContentType: 'video/mp4', 
        };
  
        const result = await s3.upload(params).promise();
        console.log(result.Location);
        Alert.alert('업로드 성공', '동영상이 성공적으로 업로드되었습니다.');
      } catch (error) {
        console.error(error);
        Alert.alert('업로드 실패', '동영상을 업로드하지 못했습니다.');
      }
    } else {
      console.error('유효하지 않은 URI 또는 파일 이름입니다.');
    }
  };

  return (
    <Container>
      <Button title="동영상 선택" onPress={showPicker} />
    </Container>
  );
};

const Container = Styled.View`
  flex: 1;
  align-items: center;
  justify-content: center;
`;

export default Camera;
