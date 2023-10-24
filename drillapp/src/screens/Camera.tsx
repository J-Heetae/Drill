import React, { useState } from 'react';
import {Alert, Image, Button} from 'react-native';

import Styled from 'styled-components/native';

import {launchCamera, launchImageLibrary} from 'react-native-image-picker';
import { PermissionsAndroid } from 'react-native';

const Camera = () => {
  const showPicker = async () => {
    const grantedcamera = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.CAMERA,
      {
        title: "App Camera Permission",
        message: "App needs access to your camera",
        buttonNeutral: "Ask Me Later",
        buttonNegative: "Cancel",
        buttonPositive: "OK"
      }
    );

    const grantedstorage = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
      {
        title: "App Camera Permission",
        message: "App needs access to your camera",
        buttonNeutral: "Ask Me Later",
        buttonNegative: "Cancel",
        buttonPositive: "OK"
      }
    );
    if (grantedcamera === PermissionsAndroid.RESULTS.GRANTED && grantedstorage === PermissionsAndroid.RESULTS.GRANTED) {
      console.log("Camera & storage permission given");
    }
    else {
      console.log("Camera permission denied");
    }
    Alert.alert(
      "선택하세요",
      "카메라 or 앨범",
      [
        {
          text: "카메라로 찍기",
          onPress: async() =>{
            const result = await launchCamera({
              mediaType : 'photo', 
              cameraType : 'back', 
            });
              if (result.didCancel){ 
                return null;
              }
              // const localUri = result.assets[0].uri;
              // const uriPath = localUri.split("//").pop();
              // const imageName = localUri.split("/").pop();
              // setPhoto("file://"+uriPath);
          }
        },
        {
          text: "앨범에서 선택",
          onPress: async() =>{
            const result = await launchImageLibrary({
              mediaType : 'photo', 
            });
            if (result.didCancel){
              return null;
            } 
            // const localUri = result.assets[0].uri;
            // const uriPath = localUri.split("//").pop();
            // const imageName = localUri.split("/").pop();
            // setPhoto("file://"+uriPath);
          }
        },
      ],
      {cancelable: false}
    );
  };
  
  
  return (
    <Container>
      <Button title="선택" onPress={showPicker} ></Button> 
    </Container>
  );
};

const Container = Styled.View`
  flex: 1;
  align-items: center;
  justify-content: center;
`;

export default Camera;