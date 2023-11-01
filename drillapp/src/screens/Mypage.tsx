import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

const BASE_URI = 'https://source.unsplash.com/random?sig=';

const Mypage = () => {
  const [data, setData] = useState<number[]>([]);

  useEffect(() => {
    fetchMore();
  }, []);
  const fetchMore = () => {
    setData(prevState => [
      ...prevState,
      ...Array.from({ length: 20 }).map((_, i) => i + 1 + prevState.length),
    ]);
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