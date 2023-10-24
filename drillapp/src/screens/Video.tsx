import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

const BASE_URI = 'https://source.unsplash.com/random?sig=';

const Video = () => {
  const [text, setText] = useState('');
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

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  return(
    <ContainerView>
      <TopView>
       <TextInput
          onChangeText={onChangeText}
          value={text}
          placeholder='검색'
          style={styles.input}  
        />
        <SortMenuView>
          <SortMenu><Text>지점</Text></SortMenu>
          <SortMenu><Text>색</Text></SortMenu>
          <SortMenu><Text>최신순</Text></SortMenu>
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
  input: {
    height: 50,
    width: 350,
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 15,
    paddingHorizontal: 8,
    marginTop: 30,
    marginBottom: 20,
  },
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
  justify-content: center;
  align-items: center;
`;
const BottomView = styled.View`
  flex: 3;
`;
// -------------------------------

const SortMenuView = styled.View`
  flex:1 ;
  display: flex;
  flex-direction: row;
  gap: 10px;
`
const SortMenu = styled.View`
  background-color: #5AC77C;
  width: 110px;
  height: 40px;
  borderRadius: 50px;
  justify-content: center;
  align-items: center;
`
export default Video;