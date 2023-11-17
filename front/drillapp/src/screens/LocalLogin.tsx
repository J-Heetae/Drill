import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Image, TextInput, Alert,KeyboardAvoidingView,ScrollView, Pressable, Text, StyleSheet } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import { useDispatch, useSelector } from "react-redux";
import { setAccessToken, setRefreshToken } from '../modules/redux/slice/TemplateUserSlice';
import { API_URL_Local } from "@env";
import { setNickName,setPlace } from "../modules/redux/slice/TemplateUserSlice";


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
        const nickname = response.data.split(" ")[0];
        const place = response.data.split(" ")[1]
        dispatch(setNickName(nickname));
        dispatch(setPlace(place))
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
      console.log('로그인 성공', response)

    } catch (error) {
      console.log('이메일--------------------', username)
      console.log('비밀번호------------------', password)
      console.error('로그인 실패', error);
      Alert.alert('로그인 실패', '아이디 또는 비밀번호가 올바르지 않습니다.');
    }
  };

  return (
    // <KeyboardAvoidingView>
      <ContainerView>
        <ScrollView>
          <LogoView>
            <Image
              source={require('../asset/icons/DRILL_green.png')}
              resizeMode="contain"
              style={{
                width: 500,
                height: 550,
                alignSelf: 'center',
              }}
            />
          </LogoView>
          <TextInput
            placeholder="아이디"
            placeholderTextColor='black'
            value={username}
            onChangeText={(text) => setUsername(text)}
            style={{ height: 60, borderColor: 'gray', borderWidth: 1, margin: 10, padding: 10, color: 'black', fontSize:20 }}
          />
          <TextInput
            placeholder="비밀번호"
            placeholderTextColor='black'
            value={password}
            onChangeText={(text) => setPassword(text)}
            secureTextEntry
            style={{ height: 60, borderColor: 'gray', borderWidth: 1, margin: 10, padding: 10, color: 'black', fontSize:20 }}
          />
          <Pressable style={styles.button} onPress={handleLogin}>
            <Text style={styles.text}>로그인</Text>
          </Pressable>
        </ScrollView>
      </ContainerView>
    // </KeyboardAvoidingView>
  );
};
const styles = StyleSheet.create({
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 16,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: '#5AC77C',
  },
  text: {
    fontSize: 18,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white',
  },
});

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