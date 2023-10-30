import React, { useState } from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import TabNavigator from "./Navigator";
import Login from "./src/screens/Login";
import Nickname from "./src/screens/Nickname";
import Freplace from "./src/screens/Freplace";
import Upload from "./src/screens/Upload";
import { Provider } from 'react-redux';
import {Store} from './src/modules/redux/Store';

function App() {
  const Stack = createNativeStackNavigator();
  const [accessToken, setAccessToken] = useState("");

  return (
    <Provider store={Store}>
      <NavigationContainer>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          {accessToken ? (
            <Stack.Screen name="TabNavigator" component={TabNavigator} />
          ) : (
            <Stack.Screen name="Login" component={Login} />
          )}
          <Stack.Screen name="Nickname" component={Nickname} />
          <Stack.Screen name="Freplace" component={Freplace} />
          <Stack.Screen name="Upload" component={Upload} />
          <Stack.Screen name="TabNavigator" component={TabNavigator} />
        </Stack.Navigator>
      </NavigationContainer>
    </Provider>
  );
}

export default App;
