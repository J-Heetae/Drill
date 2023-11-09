import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Image, TextInput, Alert,ScrollView,KeyboardAvoidingView} from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";


type RootStackParamList = {
  Nickname: undefined;
  TabNavigator: undefined;
};

const LocalLogin = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Nickname','TabNavigator'>>();
  const API_URL = 'http://10.0.2.2:8060/api/member/locallogin'; 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

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

      // 요청 성공
      console.log('로그인 성공', response.data);

    
      await AsyncStorage.setItem('accessToken', response.headers.authorization);
      navigation.navigate("Nickname");

    } catch (error) {
      console.log('이메일--------------------', username)
      console.log('비밀번호------------------', password)
      console.error('로그인 실패', error);
      Alert.alert('로그인 실패', '아이디 또는 비밀번호가 올바르지 않습니다.');
    }
  };

  return (
    <KeyboardAvoidingView>
      

    <ContainerView>
      <ScrollView>
          <LogoView>
            <Image
              source={require('../asset/icons/DRILL_green.png')}
              resizeMode="contain"
              style={{
                width: 500,
                height: 322,
                alignSelf: 'center',
              }}
            />
          </LogoView>
          <TextInput 
            placeholder="아이디"
            value={username}
            onChangeText={(text) => setUsername(text)}
            style={{ height: 40, borderColor: 'gray', borderWidth: 1, margin: 10, padding: 10}}
          />
          <TextInput
            placeholder="비밀번호"
            value={password}
            onChangeText={(text) => setPassword(text)}
            secureTextEntry
            style={{ height: 40, borderColor: 'gray', borderWidth: 1, margin: 10, padding: 10 }}
          />
          <LoginButton onPress={handleLogin} title="로그인" />

      </ScrollView>
    </ContainerView>
    

    </KeyboardAvoidingView>
  );
};

const ContainerView = styled.View`
  background-color: white;
  justify-content: center;
`;

const LogoView = styled.View`
  justify-content: center;
  align-items: center;
`;

const LoginButton = styled.Button`
  margin: 10px;
`;

export default LocalLogin;