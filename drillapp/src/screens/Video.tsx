import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { useSelector } from "react-redux";
import AWS from 'aws-sdk';

AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});

const s3 = new AWS.S3();

const Video = () => {
  const [text, setText] = useState('');
  const userInfo = useSelector((state: RootState) => state.templateUser);

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  // S3 Bucket 에서 Thumbnail 이미지 불러오기
  useEffect(() => {
    fetchS3Images();
  }, []);

  const [imageList, setImageList] = useState<string[]>([]);

  const fetchS3Images = async () => {
    try {
      const params = {
        Bucket: 'drill-video-bucket',
        Prefix: 'Thumbnail/',
      };

      const data = await s3.listObjectsV2(params).promise();
      const images = data.Contents?.map((item) => item.Key || '');
      console.log(images)
      if (images) {
        setImageList(images);
      }
      console.log(imageList);

    } catch (error) {
      console.error('Error fetching S3 images: ', error);
    }
  };

  const renderImage = ({ item }: { item: string }) => {
    const imageUrl = `https://drill-video-bucket.s3.ap-northeast-2.amazonaws.com/${item}`;
      return <Image source={{ uri: imageUrl }} style={{ width: 100, height: 100, margin: 1 }} />;
  };
  const imageUrl2 = 'https://drill-video-bucket.s3.ap-northeast-2.amazonaws.com/Thumbnail/holder.jpg';
  // --------------------------------------------

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
      const response = await axios.post(API_URL, entirePostPageDto, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 성공
      console.log('코스 목록:', response.data)
      console.log('게시글 목록:', response.data.postPage.content[0]);
    } catch (error) { 
      // 요청
      console.error('게시글 목록을 불러오는 데 실패:', error);
    }
  };

  useEffect(() => {
    // 컴포넌트가 마운트될 때 데이터를 불러오기 위해 useEffect를 사용합니다.
    fetchPostList();
  }, []);

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
          data={imageList}
          renderItem={renderImage}
          keyExtractor={(item, index) => index.toString()}
          numColumns={3}
          />
          {/* <Image source={{ uri: imageUrl2 }} style={{ width: 100, height: 100, margin: 1 }} />
          <Image source={{ uri: BASE_URI }} style={{ width: 100, height: 100, margin: 1 }} /> */}
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