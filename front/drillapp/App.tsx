import React, { useState, useEffect } from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import TabNavigator from "./Navigator";
import Login from "./src/screens/Login";
import Nickname from "./src/screens/Nickname";
import Freplace from "./src/screens/Freplace";
import Upload from "./src/screens/Upload";
import VideoDetail from "./src/screens/VideoDetail";
import LocalLogin from "./src/screens/LocalLogin";
import Regist from "./src/screens/Regist";
import { Provider } from 'react-redux';
import {Store} from './src/modules/redux/Store';
import AsyncStorage from "@react-native-async-storage/async-storage";

function App() {
  const Stack = createNativeStackNavigator();
  const [accessToken, setAccessToken] = useState("");

  // BACKEND 소통한번 하고
  // 회원정보가 있으면 TabNavigator
  // 회원정보가 없으면 Login

  useEffect(() => {
    const retrieveAccessToken = async () => {
      try {
        const storedAccessToken = await AsyncStorage.getItem('accessToken');
        console.log('storedAccessToken------------------------------------', storedAccessToken)
        if (storedAccessToken !== null) {
          // AsyncStorage에서 accessToken을 가져와 state에 저장
          setAccessToken(storedAccessToken);
        }
      } catch (error) {
        console.error('accessToken 검색 오류:', error);
      }
    };

    // AsyncStorage에서 accessToken을 가져온 후 state에 설정
    retrieveAccessToken();
  }, []); // useEffect가 처음에만 실행되도록 빈 배열을 전달합니다.

  console.log('accessToken---------------------------------------',accessToken)
  return (
    <Provider store={Store}>
      <NavigationContainer>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="Login" component={Login} />
          <Stack.Screen name="Regist" component={Regist} />
          <Stack.Screen name="LocalLogin" component={LocalLogin} />
          <Stack.Screen name="Nickname" component={Nickname} />
          <Stack.Screen name="Freplace" component={Freplace} />
          <Stack.Screen name="Upload" component={Upload} />
          <Stack.Screen name="TabNavigator" component={TabNavigator} />
          <Stack.Screen name="VideoDetail" component={VideoDetail} />

        </Stack.Navigator>
      </NavigationContainer>
    </Provider>
  );
}

export default App;
