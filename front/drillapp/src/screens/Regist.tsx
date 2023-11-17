import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Image, TouchableOpacity, TextInput, Button, Alert,KeyboardAvoidingView,ScrollView, Text, Pressable, StyleSheet} from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import { API_URL_Local } from "@env";

type RootStackParamList = {
  LocalLogin: undefined;
  TabNavigator: undefined;
};

const Regist = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'LocalLogin','TabNavigator'>>();
  const API_URL = `${API_URL_Local}member/regist`; 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  

  const LocalRegisterDto = {
    email: username,
    password: password,
  };

  const handleLogin = async () => {
    if (password !== confirmPassword) {
        Alert.alert('비밀번호가 일치하지 않습니다.');
        return;
      }
    try {
      const response = await axios.post(API_URL, LocalRegisterDto, {
        headers: {
          'Content-Type': 'application/json',
        },
      } );

      // 요청 성공
      console.log('-----------------------',response)
      console.log('로그인해 주세요.', response.data);

    
      // await AsyncStorage.setItem('accessToken', response.headers.authorization);
      navigation.navigate("LocalLogin");

    } catch (error) {

      console.error('회원가입 실패', error);
      Alert.alert('회원가입 실패');
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
                height: 470,
                alignSelf: 'center',
              }}
            />
          </LogoView>
          <TextInput
            placeholder="이메일"
            placeholderTextColor='black'
            value={username}
            onChangeText={(text) => setUsername(text)}
            style={{ height: 60, borderColor: 'black', borderWidth: 1, margin: 10, padding: 10, color: 'black', fontSize:20}}
          />
          <TextInput
            placeholder="비밀번호"
            placeholderTextColor='black'
            value={password}
            onChangeText={(text) => setPassword(text)}
            secureTextEntry
            style={{ height: 60, borderColor: 'black', borderWidth: 1, margin: 10, padding: 10, color: 'black', fontSize:20 }}
          />
          <TextInput
            placeholder="비밀번호 확인"
            placeholderTextColor='black'
            value={confirmPassword}
            onChangeText={(text) => setConfirmPassword(text)}
            secureTextEntry
            style={{ height: 60, borderColor: 'black', borderWidth: 1, margin: 10, padding: 10, color: 'black', fontSize:20 }}
          />
          <Pressable style={styles.button} onPress={handleLogin}>
            <Text style={styles.text}>회원가입</Text>
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

export default Regist;