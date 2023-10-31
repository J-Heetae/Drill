import React from "react";
import { StyleSheet, Text, View, Image, TouchableOpacity } from "react-native";
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import MainPage from './src/screens/Main';
import Mypage from "./src/screens/Mypage";
import VideoPage from "./src/screens/Video";
import CameraPage from "./src/screens/Camera";
import CalendarPage from "./src/screens/Calendar";

const Tab = createBottomTabNavigator();

const CustomTabBarButton = ({children, onPress}) => (
  <TouchableOpacity
    style={{
      top: -20,
      justifyContent: 'center',
      alignItems: 'center',
      ...styles.shadow
    }}
    onPress={onPress}
  >
    <View style={{
      width: 60,
      height: 60,
      borderRadius: 50,
      backgroundColor: '#5AC77C'
    }} >
      {children}
    </View>
  </TouchableOpacity>
);

function BottomTabNavigationApp() {
  return (
    <Tab.Navigator
      initialRouteName={"Home"}
      screenOptions={() => ({
        tabBarActiveTintColor: 'tomato',
        tabBarInactiveTintColor: 'gray',
        tabBarShowLabel: false, 
        tabBarStyle: {
          backgroundColor: '#ffffff',
          height: 60,
          borderTopColor: 'gray',
          ...styles.shadow
        }
      })}
    >
      <Tab.Screen
        name="Home"
        component={MainPage}
        options={{
          tabBarLabel: '홈',
          tabBarIcon: ({focused}) => (
            focused ?
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
              <Image
                source={require('../drillapp/src/asset/icons/Home_Active.png')}
                resizeMode="contain"
                style={{
                  width: 25,
                  height: 25,
                  tintColor: focused ? '#377A4C' : '#ADA4A5'
                }}
              />
            </View> :
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
            <Image
              source={require('../drillapp/src/asset/icons/Home.png')}
              resizeMode="contain"
              style={{
                width: 25,
                height: 25,
                tintColor: focused ? '#377A4C' : '#ADA4A5'
              }}
            />
            </View>
          ),
          headerShown: false,
        }}
      />
      <Tab.Screen
        name="Video"
        component={VideoPage}
        options={{
          tabBarLabel: '비디오',
          tabBarIcon: ({focused}) => (
            focused ?
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
              <Image
                source={require('../drillapp/src/asset/icons/Search_Active.png')}
                resizeMode="contain"
                style={{
                  width: 25,
                  height: 25,
                  tintColor: focused ? '#377A4C' : '#ADA4A5'
                }}
              />
            </View> :
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
            <Image
              source={require('../drillapp/src/asset/icons/Search.png')}
              resizeMode="contain"
              style={{
                width: 25,
                height: 25,
                tintColor: focused ? '#377A4C' : '#ADA4A5'
              }}
            />
            </View>
          ),
          headerShown: false,
        }}
      />
      <Tab.Screen 
        name='Camera'
        component={CameraPage}
        options={{
          tabBarIcon: ({focused}) => (
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
              <Image
                source={require('../drillapp/src/asset/icons/Camera.png')}
                resizeMode="contain"
                style={{
                  width: 25,
                  height: 25,
                  tintColor: focused ? '#377A4C' : '#ffffff',
                }}
              />
            </View>
          ),
          headerShown: false,
          tabBarButton: (props) => (
            <CustomTabBarButton {...props} />
          )
        }}
      />
      <Tab.Screen
        name="Calendar"
        component={CalendarPage}
        options={{
          tabBarLabel: '달력',
          tabBarIcon: ({focused}) => (
            focused ?
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
              <Image
                source={require('../drillapp/src/asset/icons/Calendar_Active.png')}
                resizeMode="contain"
                style={{
                  width: 25,
                  height: 25,
                  tintColor: focused ? '#377A4C' : '#ADA4A5'
                }}
              />
            </View> :
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
            <Image
              source={require('../drillapp/src/asset/icons/Calendar.png')}
              resizeMode="contain"
              style={{
                width: 25,
                height: 25,
                tintColor: focused ? '#377A4C' : '#ADA4A5'
              }}
            />
            </View>
          ),
          headerShown: false,
        }}
      />
      <Tab.Screen
        name="Mypage"
        component={Mypage}
        options={{
          tabBarLabel: '마이페이지',
          tabBarIcon: ({focused}) => (
            focused ?
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
              <Image
                source={require('../drillapp/src/asset/icons/Profile_Active.png')}
                resizeMode="contain"
                style={{
                  width: 25,
                  height: 25,
                  tintColor: focused ? '#377A4C' : '#ADA4A5'
                }}
              />
            </View> :
            <View style={{alignItems: 'center', justifyContent: 'center'}}>
            <Image
              source={require('../drillapp/src/asset/icons/Profile.png')}
              resizeMode="contain"
              style={{
                width: 25,
                height: 25,
                tintColor: focused ? '#377A4C' : '#ADA4A5'
              }}
            />
            </View>
          ),
          headerShown: false,
        }}
      />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  shadow: {
    shadowColor: '#7F5DF0',
    shadowOffset: {
      width: 0,
      height: 10,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.5,
    elevation: 5
  }
});

export default BottomTabNavigationApp;