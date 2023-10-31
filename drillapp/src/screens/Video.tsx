import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import axios from 'axios';

const BASE_URI = 'https://source.unsplash.com/random?sig=';
const imageUrl = 'https://source.unsplash.com/random?sig=1';

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

  const API_URL = 'http://10.0.2.2:8060/api/post/list';

  // EntirePostPageDto 객체 생성
  const entirePostPageDto = {
    centerName: 'center0',
    courseName: 'course0',
    difficulty: 'difficulty0',
    memberNickname: '',
    order: 'sss',
    page: 0,
    size: 10,
  };

  // POST 요청 보내기
  const fetchPostList = async () => {
    try {
      const response = await axios.post(API_URL, entirePostPageDto);
      // 성공
      console.log('코스 목록:', response.data)
      console.log('게시글 목록:', response.data.postPage);
    } catch (error) { 
      // 요청
      console.error('게시글 목록을 불러오는 데 실패:', error);
    }
  };

  // fetchPostList 함수 호출
  fetchPostList();

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
    height: 40,
    width: 280,
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
  width: 85px;
  height: 30px;
  borderRadius: 50px;
  justify-content: center;
  align-items: center;
`
export default Video;