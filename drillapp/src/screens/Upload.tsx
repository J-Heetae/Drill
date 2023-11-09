import React, { useState } from 'react';
import styled from 'styled-components/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { TouchableOpacity, TouchableHighlight, Text, TextInput, StyleSheet, Alert, Image, View, Button } from 'react-native';
import { useNavigation } from "@react-navigation/native";
import { launchImageLibrary} from 'react-native-image-picker';
import { ImagePickerResponse } from 'react-native-image-picker';
import { Dropdown } from 'react-native-element-dropdown';
import AWS from 'aws-sdk';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { useSelector } from "react-redux";
import Modal from 'react-native-modal';
import { API_URL_Local } from "@env";

AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});

type RootStackParamList = {
  Video: undefined;
};
type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};

const s3 = new AWS.S3();

const Upload = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Video'>>()
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const [isHovered, setIsHovered] = useState(false);
  const [text, setText] = useState('');
  const [postThumbnail, setPostThumbnail] = useState<string | undefined>(undefined);
  const [postVideo, setPostVideo] = useState<string | undefined>(undefined);
  const [videourl, setVideourl] = useState('');

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  const [selectedCenter, setSelectedCenter] = useState("지점 선택");
  const [selectedHolder, setSelectedHolder] = useState("홀드");

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
    {key:'difficulty1Course1',value:'하양'},
    {key:'difficulty1Course2',value:'노랑'},
    {key:'difficulty1Course3',value:'주황'},
    {key:'difficulty2Course1',value:'초록'},
    {key:'difficulty2Course2',value:'하양'},
    {key:'difficulty2Course3',value:'노랑'},
    {key:'difficulty3Course1',value:'주황'},
    {key:'difficulty3Course2',value:'초록'},
  ];

  const postDto = {
    memberNickname: userInfo.nickName,
    center: selectedCenter,
    postContent: text,
    postVideo: postVideo,
    courseName: selectedHolder,
    postThumbnail: postThumbnail,
  };

  const uploadVideoToS3 = async (uri: string | undefined, fileName: string | undefined) => {
    if (uri && fileName) {  
      try {
        const response = await fetch(uri);
        const blob = await response.blob();
        const now = new Date();
        const year =now.getFullYear();
        const month = now.getMonth()+1;
        const day = now.getDate();
        const hour = now.getHours();
        const minute = now.getMinutes();
        const second = now.getSeconds();
        const params = {
          Bucket: 'drill-video-bucket', 
          Key: `Video/${userInfo.nickName}_${year}${month}${day}_${hour}${minute}${second}_${fileName}`,
          Body: blob,
          ContentType: 'video/mp4', 
        };
  
        const result = await s3.upload(params).promise();
        console.log(result.Location);
        Alert.alert('업로드 성공', '동영상이 성공적으로 업로드되었습니다.');
        toggleModal();
      } catch (error) {
        console.error(error);
        Alert.alert('업로드 실패', '동영상을 업로드하지 못했습니다.');
      }
    } else {
      console.error('유효하지 않은 URI 또는 파일 이름입니다.');
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
      console.log('파일이름---------------------------', fileName);
      console.log('URI---------------------------', uri);
      setPostVideo(fileName)
      const fileName_re = fileName?.replace(".mp4", ".jpg")
      console.log("바뀐 파일이름", fileName_re)
      setPostThumbnail(fileName_re)
      uploadVideoToS3(uri, fileName);
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
      const response = await axios.post(API_URL, postDto, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 요청 성공
      console.log('게시물 게시 성공', response);
      Alert.alert('게시글 업로드 성공!');
      setVideourl('');
      setText('');
      setSelectedCenter('지점 선택');
      setSelectedHolder('홀드')
      navigation.navigate("Video");
    } catch (error) {
      // 요청 실패
      console.error('게시물 게시 실패', error);
    }
  };

  // 모달 상태 변경
  const [isModalVisible, setModalVisible] = useState(false);

  const toggleModal = () => {
    setModalVisible(!isModalVisible);
  };


  return (
    <ContainerView>
      <TopView>
        <TouchableHighlight
          onPress={() => { UploadVideo() }}
          underlayColor="#eee"
          onShowUnderlay={() => setIsHovered(true)}
          onHideUnderlay={() => setIsHovered(false)}
        >
          <UploadView>
            {videourl ? (
              <Image source={{ uri: videourl }} style={{ width: 280, height: 180 }} />
              ) : (
              <UploadText>영상을 선택해주세요</UploadText>
            )}
              <Modal isVisible={isModalVisible}>
                <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                  <View style={{ width: 200, height: 200, backgroundColor: 'white', justifyContent: 'center', alignItems: 'center' }}>
                    <Text>홀드 색상 선택</Text>
                    <Button title="Hide modal" onPress={toggleModal} />
                  </View>
                </View>
              </Modal>
          </UploadView> 
        </TouchableHighlight>
      </TopView>

      <BottomView>
        <ExplainView>
          <TextInput
            multiline
            onChangeText={onChangeText}
            value={text}
            placeholder='문구를 입력해주세요'
            style={styles.input}  
          />
        </ExplainView>
        <SelectView>
          <SelectOptionView>
            <Dropdown 
            style={styles.dropdown1}
            placeholderStyle={styles.placeholderStyle}
            selectedTextStyle={styles.selectedTextStyle}
            inputSearchStyle={styles.inputSearchStyle}
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
        <ButtonView>
          <TouchableOpacity
            style={styles.button}
            onPress={Uploadpost}>
            <Text style={styles.buttonText}>게시글 업로드</Text>
          </TouchableOpacity>
        </ButtonView>
      </BottomView>
    </ContainerView>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#5AC77C', // 버튼 배경색상 추가
    paddingVertical: 10,
    borderRadius: 30,
  },
  buttonText: {
    color: '#fff', // 버튼 글자색상 추가
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  input: {
    height: 80,
    width: 280,
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
    width: 130,
    height: 40,
    borderWidth: 1,
    borderRadius: 10,
    borderColor: '#ADA4A5',
  },
  dropdown2: {
    width: 65,
    height: 40,
    borderWidth: 1,
    borderRadius: 10,
    borderColor: '#ADA4A5',
  },
  placeholderStyle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#ADA4A5',
  },
  selectedTextStyle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#ADA4A5',
  },
  inputSearchStyle: {
    height: 40,
    fontSize: 16,
  },
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

const UploadView = styled.View`
  width: 280px;
  height: 180px;
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
  width: 280px;
  justify-content: center;
`
export default Upload;