import React, { useState } from 'react';
import styled from 'styled-components/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { TouchableOpacity, TouchableHighlight, Text, TextInput, StyleSheet } from 'react-native';
import { useNavigation } from "@react-navigation/native";

type RootStackParamList = {
  Freplace: undefined;
};

const Upload = () => {
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Freplace'>>()
  const [isHovered, setIsHovered] = useState(false);
  const [text, setText] = useState('');

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  return (
    <ContainerView>
      <TopView>
        <TouchableHighlight
          onPress={() => { /* Handle onPress */ }}
          underlayColor="#eee"
          onShowUnderlay={() => setIsHovered(true)}
          onHideUnderlay={() => setIsHovered(false)}
        >
          <UploadView>
            <UploadText>영상을 선택하세요</UploadText>
          </UploadView> 
        </TouchableHighlight>
      </TopView>

      <BottomView>
        <ExplainView>
          <TextInput
            onChangeText={onChangeText}
            value={text}
            placeholder='텍스트를 입력해주세요'
            style={styles.input}  
          />
        </ExplainView>
        <SelectView>
          <SelectPlaceView>
            <Text>지점을 선택해주세요</Text>
          </SelectPlaceView>
          <SelectDiffView>
            <Text>난이도를 선택해주세요</Text>
          </SelectDiffView>
        </SelectView>
        <ButtonView>
          <TouchableOpacity
            style={styles.button}
            onPress={() => navigation.navigate('Freplace')}>
            <Text style={styles.buttonText}>다음</Text>
          </TouchableOpacity>
        </ButtonView>
      </BottomView>
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
    height: 100,
    width: 300,
    borderColor: 'white',
    borderWidth: 1,
    paddingHorizontal: 8,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.18,
    shadowRadius: 1.00,

    elevation: 1,
  },
});

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
  align-items: center;
`
// -------------------------------
const TopView = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
`;
const BottomView = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
`;
// -------------------------------

const UploadView = styled.View`
  width: 300px;
  height: 220px;
  border-radius: 10px;
  border: 1px dashed #999;
  justify-content: center;
  align-items: center;
`
const UploadText = styled.Text`
  font-size: 20px;
`
// -------------------------------

const ExplainView = styled.View`
  flex: 1;
`
const SelectView = styled.View`
  flex: 1;
  gap: 10px
`
const SelectPlaceView = styled.View`
`
const SelectDiffView = styled.View`
`
const ButtonView = styled.View`
  flex: 1;
  width: 350px;
  justify-content: center;
`
export default Upload;