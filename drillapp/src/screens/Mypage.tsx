import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text, Button } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import AsyncStorage from "@react-native-async-storage/async-storage";
import { RootState } from "../modules/redux/RootReducer";
import Login from './Login';
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";

const BASE_URI = 'https://source.unsplash.com/random?sig=';

type RootStackParamList = {
  Login: undefined;
};

const Mypage = () => {
  const [data, setData] = useState<number[]>([]);
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Login'>>();
  useEffect(() => {
    fetchMore();
  }, []);
  const fetchMore = () => {
    setData(prevState => [
      ...prevState,
      ...Array.from({ length: 20 }).map((_, i) => i + 1 + prevState.length),
    ]);
  };

  const Logout = async () => {
    try {
      // AsyncStorage에서 accessToken 값을 빈 문자열로 설정
      await AsyncStorage.setItem('accessToken', '');
      // 로그아웃 후 필요한 작업 수행
      navigation.navigate("Login");
    } catch (error) {
      console.error('로그아웃 오류:', error);
    }
  };

  return (
    <ContainerView>
      <TopView>
        <UserInfoView>
          <UserNameText>
            <Image
              source={require('../asset/icons/profile_hold.png')}
              resizeMode="contain"
              style={{
                width: 40,
                height: 40,
              }}
            />
            DRILL
            <Button
              title="로그아웃"
              onPress={Logout}
            />
          </UserNameText>
          <UserExpView>

          </UserExpView>
        </UserInfoView>
        <SortMenuView>
          <SortMenuTitle>내 영상 모아보기</SortMenuTitle>
          <MenuView>
            <SortMenu><Text>지점</Text></SortMenu>
            <SortMenu><Text>색</Text></SortMenu>
            <SortMenu><Text>최신순</Text></SortMenu>
          </MenuView>
        </SortMenuView>
      </TopView>

      <BottomView>
        <SafeAreaView>
          <FlatList
            data={data}
            style={styles.list}
            numColumns={3}
            onEndReached={fetchMore}
            keyExtractor={e => e.toString()}
            renderItem={({item}) => (
              <Image source={{uri: BASE_URI + item}} style={styles.item} />
            )}
          />
        </SafeAreaView>
      </BottomView>
    </ContainerView>
  );
};

const styles = StyleSheet.create({
  list: {
    width: '100%',
  },
  item: {
    aspectRatio: 1,
    width: '100%',
    flex: 1,
  }
});

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
`
// -------------------------------

const TopView = styled.View`
  flex: 1;
  gap: 10px;
`;
const BottomView = styled.View`
  flex: 1.8;
`;
// -------------------------------

const UserInfoView = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
  gap: 10px;
`
const SortMenuView = styled.View`
  flex: 1;
  padding-left: 30px;
  gap: 10px;
`
const UserNameText = styled.Text`
  font-size: 25px;
  text-align: center;
  font-weight: 900;
  color: black;
`
const UserExpView = styled.View`
  width: 280px;
  height: 30px;
  background-color: #DDDADA;
  border-radius: 30px;
`
// -------------------------------

const SortMenuTitle = styled.Text`
  font-size: 20px;
`
const MenuView = styled.View`
  flex:1 ;
  display: flex;
  flex-direction: row;
  gap: 10px;
`
const SortMenu = styled.View`
  width: 80px;
  height: 40px;
  background-color: #5AC77C;
  border-radius: 50px;
  justify-content: center;
  align-items: center;
`
export default Mypage;