import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Image, TouchableOpacity, TextInput, Button, Alert } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

type RootStackParamList = {
  Nickname: undefined;
  TabNavigator: undefined;
};

const LocalLogin = () => {
  const API_URL = 'http://10.0.2.2:8060/api/member/login'; 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    try {
      const response = await axios.post(API_URL, {
        username,
        password,
      });

      // 요청 성공
      console.log('로그인 성공', response.data);

    
      await AsyncStorage.setItem('accessToken', response.headers.authorization);


    } catch (error) {
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