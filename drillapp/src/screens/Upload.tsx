import React, { useState, useEffect } from 'react';
import styled from 'styled-components/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { TouchableOpacity, TouchableHighlight, Text, TextInput, StyleSheet, Alert, Image, View, Button,ScrollView, Platform } from 'react-native';
import { useNavigation } from "@react-navigation/native";
import { launchImageLibrary} from 'react-native-image-picker';
import { ImagePickerResponse } from 'react-native-image-picker';
import { Dropdown } from 'react-native-element-dropdown';
import AWS from 'aws-sdk';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { useSelector } from "react-redux";
import { API_URL_Local } from "@env";
import Snackbar from "react-native-snackbar"

AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});

type RootStackParamList = {
  Video: undefined;
  Home: undefined;
};
type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};

const s3 = new AWS.S3();

const Upload = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Video', 'Home'>>()
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const [isHovered, setIsHovered] = useState(false);
  const [text, setText] = useState('');
  const [postThumbnail, setPostThumbnail] = useState<string | undefined>(undefined);
  const [postVideo, setPostVideo] = useState<string | undefined>(undefined);
  const [postUri, setPostUri] = useState<string | undefined>(undefined);
  const [videourl, setVideourl] = useState('');
  const [pythonPostVideo, setPythonPostVideo] = useState<string | undefined>(undefined);

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  const [selectedCenter, setSelectedCenter] = useState("center1");
  const [selectedHolder, setSelectedHolder] = useState("S1하양연두_HD");

  const data: DataItem[] = [
    {key:'center1',value:'더클라임 홍대'},
    {key:'center2',value:'더클라임 일산'},
    {key:'center3',value:'더클라임 양재'},
    {key:'center4',value:'더클라임 마곡'},
    {key:'center5',value:'더클라임 신림'},
    {key:'center6',value:'더클라임 연남'},
    {key:'center7',value:'더클라임 강남'},
    {key:'center8',value:'더클라임 사당'},
    {key:'center9',value:'더클라임 신사'},
    {key:'center10',value:'더클라임 서울대'},
  ];
  const holderColor: DataItem[] = [
    {key:'S1하양연두_HD',value:'하양'},
    {key:'S2노랑노랑_HD',value:'노랑'},
    {key:'S3주황초록_HD',value:'주황'},
    {key:'S4초록검정_HD',value:'초록'},
    {key:'S5파랑빨강_HD',value:'하양'},
    {key:'S1빨강핑크_HD',value:'노랑'},
    {key:'S2보라파랑_HD',value:'주황'},
    {key:'S3회색민트_HD',value:'초록'},
  ];

  const uploadVideoToS3 = async (uri: string | undefined, fileName: string | undefined) => {
    if (uri && fileName) {  
      try {
        const response = await fetch(uri);
        const blob = await response.blob();
        const now = new Date();
        const year = now.getFullYear();
        const month = now.getMonth() + 1;
        const day = now.getDate();  
        const hour = now.getHours();
        const minute = now.getMinutes();
        const second = now.getSeconds();
        // 파일 이름에서 Video/ 부분을 제외한 부분을 추출
        const keyWithoutVideo = `${userInfo.nickName}_${year}${month}${day}_${hour}${minute}${second}_${fileName}`;
        
        const params = {
          Bucket: 'drill-video-bucket', 
          Key: `Video/${keyWithoutVideo}`,
          Body: blob,
          ContentType: 'video/mp4', 
        };
  
        const result = await s3.upload(params).promise();
        const fileName_replace = keyWithoutVideo?.replace(".mp4", "")
        setPythonPostVideo(fileName_replace);
        
        // 동일한 파일 이름을 사용하여 상태 업데이트
        setPostVideo(keyWithoutVideo + '.mp4');
        setPostThumbnail(keyWithoutVideo + '.jpg');
        
        VideoDownload(keyWithoutVideo);
        return keyWithoutVideo; // 업로드 성공 시 파일 이름 반환
      } catch (error) { 
        console.error(error);
        return null; // 업로드 실패 시 null 반환
      }
    } else {
      console.error('유효하지 않은 URI 또는 파일 이름입니다.');
      return null; // URI 또는 파일 이름이 유효하지 않을 경우 null 반환
    }
  };

  const handleResponse = async (response: ImagePickerResponse) => {
    if (response.didCancel) {
      console.log('취소');
    } else if (response.errorCode) {
      console.error(response.errorCode);
    } else if (response.assets && response.assets.length > 0) {
      const uri = response.assets[0].uri;
      const fileName = response.assets[0].fileName;
      setPostVideo(fileName)
      setPostUri(uri)
      const fileName_re = fileName?.replace(".mp4", ".jpg")
      setPostThumbnail(fileName_re)
      // uploadVideoToS3(uri, fileName);
      setVideourl(`https://drill-video-bucket.s3.ap-northeast-2.amazonaws.com/Thumbnail/${fileName_re}`)
    } else {
      console.error('이미지가 선택되지 않았습니다.');
    }
  };

  const UploadVideo = () => {
    launchImageLibrary({ mediaType: 'video' }, handleResponse); 
  }



  const API_URL = `${API_URL_Local}post`;
  const Uploadpost = async () => {
    try {
      const uploadedFileName = await uploadVideoToS3(postUri, postVideo);
      const fileNameWithoutMp4 = uploadedFileName?.replace(".mp4", "");

      const postDto = {
        center: selectedCenter,
        courseName: selectedHolder,
        memberNickname: userInfo.nickName,
        postContent: text,
        postThumbnail: fileNameWithoutMp4+'.jpg',  // 업로드된 파일 이름으로 수정
        postVideo: fileNameWithoutMp4+'.mp4',      // 업로드된 파일 이름으로 수정
      };
  
      const response = axios.post(API_URL, postDto, {
        headers: {
          Authorization: userInfo.accessToken,
        },
      });
  
      setVideourl('');
      setText('');
      setSelectedCenter('지점 선택');
      setSelectedHolder('홀드');
      navigation.navigate('Home');
    } catch (error) {
      console.error('게시물 게시 실패', error);
    }
  };
  
  const VideoDownload = async (fileName: string | undefined) => {
    const fileNameWithoutMp4 = fileName?.replace(".mp4", "");
    try {
      const response = await axios.get(`https://k9a106a.p.ssafy.io/video/download/${fileNameWithoutMp4}`);
      console.log('다운로드 여부', response.data.download);
      console.log('다운로드 여부', response.data.check)
      if (response.data.download) {
        // 다운로드가 성공했을 때의 동작
        VideoProcess(fileName);
      } else {
        // 다운로드가 실패했을 때의 동작
        console.log('다운로드 실패');
      }
    } catch (error) {
      console.error('영상 다운로드 실패', error);
    }
  };
  const VideoProcess = async (fileName: string | undefined) => {
    const fileNameWithoutMp4 = fileName?.replace(".mp4", "");
    try {
      const response = await axios.get(`https://k9a106a.p.ssafy.io/video/process/${fileNameWithoutMp4}`
      );
      console.log('영상 처리 성공', response)

      SnackbarOpen();
    } catch (error) {
      console.error('영상 처리 실패', error);
    }
  };
  const SnackbarOpen = () => {
    Snackbar.show({
      text: '영상이 성공적으로 게시됐습니다.',
      duration: Snackbar.LENGTH_LONG,
    });
  };

  return (
    <ContainerView>
      <ScrollView>
      <Spacer />
        <TopView>
          <CautionView>
            <Text style={{color:'black', fontSize:25}}>※ 업로드 주의사항 ※</Text>
            <Text style={{color:'black', fontSize:18}}>1. 삼각대로 고정된 상태에서 영상을 촬영해주세요.</Text>
            <Text style={{color:'black', fontSize:18}}>2. 시작 지점과 끝 지점이 화면에 나와야합니다.</Text>
            <Text style={{color:'black', fontSize:18}}>3. 업로드 영상 처리 시간이 걸리니 기다려주세요.</Text>
          </CautionView>
          <Spacer />
          <Spacer />
          <Spacer />
          <TouchableHighlight
            onPress={() => { UploadVideo() }}
            underlayColor="#eee"
            onShowUnderlay={() => setIsHovered(true)}
            onHideUnderlay={() => setIsHovered(false)}
          >
            <UploadView>
              {videourl ? (
                <UploadText  style={{color:'black'}}> {postVideo} </UploadText>
                ) : (
                <UploadText style={{color:'black'}}>영상을 선택해주세요</UploadText>
              )}
            </UploadView> 
          </TouchableHighlight>
        </TopView>

        <Spacer />

        <BottomView>
          <ExplainView>
            <TextInput
              multiline
              onChangeText={onChangeText}
              value={text}
              placeholder='문구를 입력해주세요'
              placeholderTextColor='black'
              style={styles.input}  
            />
          </ExplainView>

          <Spacer />
          <Spacer />
          <SelectView>

            <SelectOptionView>
              <Dropdown 
                style={styles.dropdown1}
                placeholderStyle={styles.placeholderStyle}
                selectedTextStyle={styles.selectedTextStyle}
                inputSearchStyle={styles.inputSearchStyle}
                itemTextStyle={styles.itemTextStyle}
                mode='default'
                data={data}
                maxHeight={200}
                placeholder='지점 선택'
                labelField="value"
                valueField="key"
                value={selectedCenter}
                onChange={(item) => {
                  const selectedOption = data.find(option => option.value === item.value);
                  setSelectedCenter(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
                }}
              />
              <Dropdown 
                style={styles.dropdown2}
                placeholderStyle={styles.placeholderStyle}
                selectedTextStyle={styles.selectedTextStyle}
                inputSearchStyle={styles.inputSearchStyle}
                itemTextStyle={styles.itemTextStyle}
                mode='default'
                data={holderColor}
                maxHeight={200}
                placeholder='홀드'
                labelField="value"
                valueField="key"
                value={selectedHolder}
                onChange={(item) => {
                  const selectedOption = holderColor.find(option => option.value === item.value);
                  setSelectedHolder(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
                }}
              />
            </SelectOptionView>
          </SelectView>
          <Spacer />
          <Spacer />
          <Spacer />
          
          <ButtonView>
          <TouchableOpacity
            style={styles.button}
            onPress={() => Uploadpost()}>
            <Text style={styles.buttonText}>게시글 업로드</Text>
          </TouchableOpacity>
          </ButtonView>
        </BottomView>
      </ScrollView>
    </ContainerView>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#5AC77C', // 버튼 배경색상 추가
    paddingVertical: 15,
    borderRadius: 30,
    ...Platform.select({
      android: {
        elevation: 3,
      },
    }),
  },
  buttonText: {
    color: '#fff', // 버튼 글자색상 추가
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  input: {
    height: 150,
    width: 380,
    fontSize: 20,
    borderColor: '#ADA4A5',
    borderWidth: 1,
    borderRadius: 10,
    paddingHorizontal: 8,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.18,
    shadowRadius: 1.00,

    elevation: 1,
  },
  dropdown1: {
    width: '50%',
    height: '110%',
    borderWidth: 1,
    borderRadius: 10,
    borderColor: '#ADA4A5',
  },
  dropdown2: {
    width: '30%',
    height: '110%',
    borderWidth: 1,
    borderRadius: 10,
    borderColor: '#ADA4A5',
  },
  placeholderStyle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#000',
  },
  selectedTextStyle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#000',
  },
  inputSearchStyle: {
    height: 40,
    fontSize: 16,
  },
  itemTextStyle: {
    color: '#000'
  }
});

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
  align-items: center;
  
`
// -------------------------------
const TopView = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
`;
const BottomView = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
`;
// -------------------------------
const CautionView = styled.View`
  width: 380px;
  height: 100px;
  justify-content: center;
  align-items: center;
`
const UploadView = styled.View`
  width: 380px;
  height: 100px;
  border-radius: 10px;
  border: 1px dashed #999;
  justify-content: center;
  align-items: center;
`
const UploadText = styled.Text`
  font-size: 20px;
`
// -------------------------------

const ExplainView = styled.View`
  flex: 1;
`
const SelectView = styled.View`
  flex: 0.3;
  gap: 10px
`

const SelectOptionView = styled.View`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 30px;
`
const ButtonView = styled.View`
  flex: 1;
  width: 380px;
  height: 100px;
  justify-content: center;
  ${Platform.OS === 'android'
    ? 'elevation: 5;'
    : 'shadowColor: #000; shadowOffset: 0 2px; shadowOpacity: 0.2; shadowRadius: 5px;'}
`

const Spacer = styled.View`
  height: 20px; /* 원하는 여백 크기 */
`;
export default Upload;