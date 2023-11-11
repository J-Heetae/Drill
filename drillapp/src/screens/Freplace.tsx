import React, { useState, useEffect } from 'react';
import styled from 'styled-components/native';
import { Text, TextInput, StyleSheet, TouchableOpacity } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import { useDispatch, useSelector } from "react-redux";
import { Dropdown } from 'react-native-element-dropdown';
import { setPlace } from '../modules/redux/slice/TemplateUserSlice';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { API_URL_Local } from "@env";

type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};

type RootStackParamList = {
  TabNavigator: undefined;
};

const Freplace = () => {
  const dispatch = useDispatch()
  const API_URL = `${API_URL_Local}member/settings`;
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'TabNavigator'>>()
  const [selectedCenter, setSelectedCenter] = useState("");
  const userInfo = useSelector((state: RootState) => state.templateUser);

  useEffect(() => {
    // selectedCenter 값이 변경될 때마다 dispatch(setPlace(selectedCenter))를 호출
    dispatch(setPlace(selectedCenter));
  }, [selectedCenter]); // selectedCenter 값이 변경될 때만 호출되도록 설정

  const settingDto = {
    memberNickname: userInfo.nickName,
    center: userInfo.place
  };
  
  const settingMyInfo = async () => {
    try {
      const response = await axios.put(API_URL, settingDto, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });

      // 요청 성공
      console.log('개인정보 저장 성공:', response.data);
    } catch (error) {
      // 요청 실패
      console.log('내 accesstoken', userInfo.accessToken);
      console.error('개인정보 저장 실패', error);
    }
  };

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

  const setUserInfo = () => {
    settingMyInfo()
    navigation.navigate('TabNavigator')
  };

  return (
    <ContainerView>
      <ContentView>
        <TitleText style={{color: 'black'}}>
          자주가는 지점을 설정해주세요.
        </TitleText>
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
      </ContentView>
      <ButtonView>
        <TouchableOpacity
          style={styles.button}>
          <Text style={styles.buttonText} onPress={setUserInfo}>다음</Text>
        </TouchableOpacity>
      </ButtonView>
    </ContainerView>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#5AC77C', // 버튼 배경색상 추가
    paddingVertical: 15,
    borderRadius: 30,
  },
  buttonText: {
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  dropdown1: {
    width: 350,
    height: 50,
    borderColor: '#000',
    borderWidth: 1,
    marginTop: 20,
  },
  placeholderStyle: {
    fontSize: 18,
    textAlign: 'center',
    color: '#000',
  },
  selectedTextStyle: {
    fontSize: 18,
    textAlign: 'center',
    color: '#000',
  },
  inputSearchStyle: {
    height: 40,
    fontSize: 18,
    color: '#000',
  },
  itemTextStyle: {
    color: '#000',
    fontSize: 18,
  }
});

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
  align-items: center;
`
// -------------------------------

const ContentView = styled.View`
  flex: 5;
  justify-content: center;
  align-items: center;
`
const ButtonView = styled.View`
  flex: 1;
  width: 350px;
  justify-content: center;
`
// -------------------------------

const TitleText = styled.Text`
  font-size: 25px;
  font-family: 'SCDream4';
`
export default Freplace;