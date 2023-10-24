import React, {FC} from 'react';
import styled from 'styled-components/native';
import {Image, TouchableOpacity} from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";

type RootStackParamList = {
  Nickname: undefined;
};

const Login = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Nickname'>>();

  const onPressMoveTab = () => {
    navigation.navigate("Nickname");
  };
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
        <LoginView>
          <TouchableOpacity onPress={onPressMoveTab}>
          <Image
            source={require('../asset/icons/kakao_login.png')}
            resizeMode="contain"
            style={{
              width: 250,
              height: 250,
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
  flex: 3;
`

const LoginView = styled.View`
  flex: 1;  
`
export default Login;