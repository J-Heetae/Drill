import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Image, TouchableOpacity, TextInput, Button, Alert } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import { useDispatch, useSelector } from "react-redux";
import { setAccessToken, setRefreshToken } from '../modules/redux/slice/TemplateUserSlice';
import { API_URL_Local } from "@env";

type RootStackParamList = {
  Nickname: undefined;
  TabNavigator: undefined;
  Freplace: undefined;
};

const LocalLogin = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Nickname','TabNavigator'>>();
  const navigation2 = useNavigation<StackNavigationProp<RootStackParamList, 'Freplace'>>();
  const API_URL = `${API_URL_Local}member/locallogin`; 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const dispatch = useDispatch()

  const LocalLoginDto = {
    email: username,
    password: password,
  };

  const handleLogin = async () => {
    try {
      const response = await axios.post(API_URL, LocalLoginDto, {
        headers: {
          'Content-Type': 'application/json',
        },
      } );

      await AsyncStorage.setItem('accessToken', response.headers.authorization);
      dispatch(setAccessToken(response.headers.authorization));
      dispatch(setRefreshToken(response.headers.refreshtoken));

      if (response.status === 200) {
        // Redirect to Main 페이지
        navigation.navigate("TabNavigator");
      } else if (response.status === 201 && response.data === '닉네임 설정 필요') {
          // Redirect to Nickname 페이지
          navigation.navigate("Nickname");
      } else if (response.status === 201 && response.data === '관심지역 설정 필요') {
          // Redirect to Freplace 페이지
          navigation2.navigate("Freplace");
      } else {
          // Handle other cases if needed
          console.log('Unknown response:', response);
      }
     

    } catch (error) {
      console.log('이메일--------------------', username)
      console.log('비밀번호------------------', password)
      console.error('로그인 실패', error);
      Alert.alert('로그인 실패', '아이디 또는 비밀번호가 올바르지 않습니다.');
    }
  };

  return (
    <ContainerView>
      <LogoView>
        <Image
          source={require('../asset/icons/DRILL_green.png')}
          resizeMode="contain"
          style={{
            width: 500,
            height: 500,
            alignSelf: 'center',
          }}
        />
      </LogoView>
      <TextInput
        placeholder="아이디"
        value={username}
        onChangeText={(text) => setUsername(text)}
        style={{ height: 40, borderColor: 'gray', borderWidth: 1, margin: 10, padding: 10 }}
      />
      <TextInput
        placeholder="비밀번호"
        value={password}
        onChangeText={(text) => setPassword(text)}
        secureTextEntry
        style={{ height: 40, borderColor: 'gray', borderWidth: 1, margin: 10, padding: 10 }}
      />
      <LoginButton onPress={handleLogin} title="로그인" />
    </ContainerView>
  );
};

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
  justify-content: center;
`;

const LogoView = styled.View`
  flex: 2;
  justify-content: center;
  align-items: center;
`;

const LoginButton = styled.Button`
  margin: 10px;
`;

export default LocalLogin;