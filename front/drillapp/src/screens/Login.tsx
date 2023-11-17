import React, { useState, useEffect } from 'react';
import styled from 'styled-components/native';
import {Image, TouchableOpacity, Text, Platform } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import  * as KakaoLogin from '@react-native-seoul/kakao-login';
import { useDispatch, useSelector } from "react-redux";
import { setAccessToken, setRefreshToken } from '../modules/redux/slice/TemplateUserSlice';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { setNickName,setPlace } from "../modules/redux/slice/TemplateUserSlice";
import { API_URL_Local } from '@env';

type RootStackParamList = {
  Nickname: undefined;
  TabNavigator: undefined;
  Regist: undefined;
  LocalLogin: undefined;
};

const Login = () => {
  const API_URL = `${API_URL_Local}member/login`;
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const dispatch = useDispatch()
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Nickname','TabNavigator'>>();
  const navigation2 = useNavigation<StackNavigationProp<RootStackParamList, 'Regist', 'LocalLogin'>>();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const onPressMoveTab = () => {
    navigation.navigate("Nickname");
  };

  useEffect(() => {
    if (isLoggedIn) {
      navigation.navigate("TabNavigator");
    }
  }, [isLoggedIn]);

  const Locallogin = () => {
    navigation2.navigate("LocalLogin");
  };
  const LocalSignup = () => {
    navigation2.navigate("Regist");
  };
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
        type: 'kakao',
        socialToken: accessToken,
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
        const nickname = response.data.split(" ")[0];
        const place = response.data.split(" ")[1]
        dispatch(setNickName(nickname));
        dispatch(setPlace(place))
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
            width: 600,
            height: 600,
            alignSelf: 'center'
          }}
        />
      </LogoView>
        <GoogleLoginView>
          <TouchableOpacity onPress={LocalSignup}>
            <Text style={{fontSize: 25, color:'#000'}}>회원가입</Text>
          </TouchableOpacity>
        </GoogleLoginView>
        <GoogleLoginView>
          <TouchableOpacity onPress={Locallogin}>
            <Text style={{fontSize: 25, color:'#000'}}>로그인</Text>
          </TouchableOpacity>
        </GoogleLoginView>
        <KakaoLoginView>
          <TouchableOpacity onPress={login}>
            <Image
              source={require('../asset/icons/kakao_login2.png')}
              resizeMode="contain"
              style={{
                width: 350,
                height: 75,
                alignSelf: 'center',
              }}
            />
          </TouchableOpacity>
        </KakaoLoginView>
    </ContainerView>
  );
};

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
  align-items: center;
`
const LogoView = styled.View`
  flex: 2;
  ${Platform.OS === 'android'
    ? 'elevation: 5;'
    : 'shadowColor: #000; shadowOffset: 0 2px; shadowOpacity: 0.2; shadowRadius: 5px;'}
`

const GoogleLoginView = styled.View`
  position: absoulte;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 300px;
  height: 65px;
  border: 1px solid gray;
  border-radius: 10px;
  margin-top: 10px;
  background-color: #5AC77C;
  ${Platform.OS === 'android'
    ? 'elevation: 5;'
    : 'shadowColor: #000; shadowOffset: 0 2px; shadowOpacity: 0.2; shadowRadius: 5px;'}
`
const KakaoLoginView = styled.View`
  position: absoulte;
  margin-bottom: 10px;
  margin-top: 10px;
`
export default Login;