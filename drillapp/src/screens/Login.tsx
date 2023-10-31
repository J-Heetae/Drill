import React, { useState, useEffect } from 'react';
import styled from 'styled-components/native';
import {Image, TouchableOpacity} from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import  * as KakaoLogin from '@react-native-seoul/kakao-login';

type RootStackParamList = {
  Nickname: undefined;
};

const Login = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Nickname'>>();
  
  const onPressMoveTab = () => {
    navigation.navigate("Nickname");
  };

  const login = () => {
    KakaoLogin.login().then((result) => {
        console.log("Login Success", JSON.stringify(result));
        getProfile();
        navigation.navigate("Nickname");
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