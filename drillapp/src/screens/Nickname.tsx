import React, { useState,ChangeEvent,useEffect  } from 'react';
import styled from 'styled-components/native';
import { Text, TextInput, StyleSheet, TouchableOpacity, Alert } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import { useDispatch, useSelector } from "react-redux";
import { setNickName } from "../modules/redux/slice/TemplateUserSlice";
import { debounce } from "lodash";
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { API_URL_Local } from "@env";


type RootStackParamList = {
  Freplace: undefined;
};

const Nickname : React.FC = () => {
  const dispatch = useDispatch()
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Freplace'>>()
  const [text, setText] = useState<string>('');
  const [inputCount, setInputCount] = useState(0);
  const [isDuplicate, setIsDuplicate] = useState(false);
  const userInfo = useSelector((state: RootState) => state.templateUser);

   // debounce 함수를 사용하여 GET 요청을 보내는 함수
   const debounceSearch = debounce(async (nickname: string) => {
      try {
        if (nickname.length > 0) {
          console.log("nickname?")
          const response = await axios.get(`${API_URL_Local}member/nickname/${nickname}`, {
            headers: {
              Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
            },
          });
          setIsDuplicate(response.data); // 백에서 받은 데이터를 상태에 설정
        }
      } catch (error) {
        // 에러 발생 시 에러 처리 코드를 작성합니다.
        console.error("Error during debounceSearch:", error);
        // 예를 들어, 에러 메시지를 표시하거나 다른 처리를 수행할 수 있습니다.
      }
    }, 100);
  


  const onChangeText = (text: string) => {
    if (text.length >= 0 && text.length <= 10) {
      setText(text);
      setInputCount(text.length);
      // debounce 함수 호출
      debounceSearch(text);
    }

  };

  useEffect(() => {
    setIsDuplicate(false);
  }, [text]);


  const setUserInfo = () => {

    if (inputCount === 0 || text.trim() === '') {
      Alert.alert("닉네임 설정이 필요합니다.");
    } else if (isDuplicate) {
      Alert.alert("중복된 닉네임이 있습니다. 변경해주세요");
    } else {
      const result = dispatch(setNickName(text))
      console.log("result :: ", result)
      navigation.navigate('Freplace')
    }
  };

  return (
    <ContainerView>
      <ContentView>
        <TitleText style={{color:'black'}}>
          닉네임을 설정해주세요.
        </TitleText>
        <TextInput
          onChange={(e) => onChangeText(e.nativeEvent.text)}
          value={text}
          placeholder='닉네임'
          placeholderTextColor='black'
          style={styles.input}  
        />
        {text.length > 0 && isDuplicate && (
          <Text>
            * 중복된 닉네임입니다. *
          </Text>
        )}
      </ContentView>
      <ButtonView>
        <TouchableOpacity
          style={styles.button}>
          <Text style={styles.buttonText} onPress={setUserInfo}>다음</Text>
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
    height: 50,
    width: 350,
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
  width: 350px;
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