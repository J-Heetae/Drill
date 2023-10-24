import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Text, TextInput, StyleSheet, TouchableOpacity } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";

type RootStackParamList = {
  Freplace: undefined;
};

const Nickname = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Freplace'>>()
  const [text, setText] = useState('');

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  return (
    <ContainerView>
      <ContentView>
        <TitleText>
          닉네임을 설정해주세요.
        </TitleText>
        <TextInput
          onChangeText={onChangeText}
          value={text}
          placeholder='닉네임'
          style={styles.input}  
        />
        <ErrorText>
          중복된 닉네임입니다.
        </ErrorText>
      </ContentView>
      <ButtonView>
        <TouchableOpacity
          style={styles.button}
          onPress={() => navigation.navigate('Freplace')}>
          <Text style={styles.buttonText}>다음</Text>
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
    color: '#fff', // 버튼 글자색상 추가
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  input: {
    height: 40,
    width: 300,
    borderColor: 'gray',
    borderWidth: 1,
    paddingHorizontal: 8,
    marginTop: 20,
    marginBottom: 20,
  },
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
  width: 85%;
  justify-content: center;
`
// -------------------------------

const TitleText = styled.Text`
  font-size: 25px;
`
const ErrorText = styled.Text`
  font-size: 18px;
  color: red;
`
export default Nickname;