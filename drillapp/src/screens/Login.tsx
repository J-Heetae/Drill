import React, { useState, useEffect } from 'react';
import styled from 'styled-components/native';
import {Image, TouchableOpacity, } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import  * as KakaoLogin from '@react-native-seoul/kakao-login';
import { useDispatch, useSelector } from "react-redux";
import { setAccessToken, setRefreshToken } from '../modules/redux/slice/TemplateUserSlice';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { setNickName } from "../modules/redux/slice/TemplateUserSlice";

type RootStackParamList = {
  Nickname: undefined;
  TabNavigator: undefined;
};

const Login = () => {
  const API_URL = 'http://10.0.2.2:8060/api/member/login';
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const dispatch = useDispatch()
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Nickname','TabNavigator'>>();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const onPressMoveTab = () => {
    navigation.navigate("Nickname");
  };

  useEffect(() => {
    if (isLoggedIn) {
      navigation.navigate("TabNavigator");
    }
  }, [isLoggedIn]);

  const login = () => {
    KakaoLogin.login().then((result) => {
      console.log("Login Success", JSON.stringify(result));
      
      getProfile();
      console.log('-----------------------------------------------------------------------------')
      loginToBackend(result.accessToken)
    }).catch((error) => {
      if (error.code === 'E_CANCELLED_OPERATION') {
          console.log("Login Cancel", error.message);
      } else {
          console.log(`Login Fail(code:${error.code})`, error.message);
      }
    });
  };
  
  const getProfile = () => {
    KakaoLogin.getProfile().then((result) => {
        console.log("GetProfile Success", JSON.stringify(result));
    }).catch((error) => {
        console.log(`GetProfile Fail(code:${error.code})`, error.message);
    });
  };

  const loginToBackend = async (accessToken: string) => {
    try {
      const response = await axios.post(API_URL, {
        kakaoToken: accessToken,
      });
  
      // 요청 성공
      console.log('로그인 성공', response.headers);
      console.log('로그인 닉네임', response.data);

      // AsyncStorage에 accessToken 저장
      await AsyncStorage.setItem('accessToken', response.headers.authorization); 
      dispatch(setAccessToken(response.headers.authorization));
      dispatch(setRefreshToken(response.headers.refreshtoken));
      if ( response.data === "닉네임 설정 필요" ) {
        navigation.navigate("Nickname");
      } else {
        dispatch(setNickName(response.data));
        setIsLoggedIn(true);
      }
      // 여기에서 프론트엔드에서 필요한 상태(state)를 업데이트하거나 다음 단계로 이동할 수 있습니다.
    } catch (error) {
      // 요청 실패
      console.error('로그인 실패', error);
      // 여기에서 오류 처리를 할 수 있습니다.
    }
  }
  return (
    <ContainerView>
      <LogoView>
       <Image
          source={require('../asset/icons/DRILL_green.png')}
          resizeMode="contain"
          style={{
            width: 500,
            height: 500,
            alignSelf: 'center'
          }}
        />
      </LogoView>
        <LoginView>
          <TouchableOpacity onPress={login}>
          <Image
            source={require('../asset/icons/kakao_login.png')}
            resizeMode="contain"
            style={{
              width: 230,
              height: 230,
              alignSelf: 'center',
            }}
          />
          </TouchableOpacity>
        </LoginView>
    </ContainerView>
  );
};

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
`
const LogoView = styled.View`
  flex: 2;
`

const LoginView = styled.View`
  flex: 1;  
`
export default Login;