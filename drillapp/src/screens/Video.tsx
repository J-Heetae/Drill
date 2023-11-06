import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text, TouchableOpacity, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { useSelector } from "react-redux";
import AWS from 'aws-sdk';
import { useNavigation } from "@react-navigation/native";
import { StackNavigationProp } from '@react-navigation/stack';

AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});
interface Post {
  center: string;
  course: {
    center: string;
    courseId: number;
    courseName: string;
    difficulty: string;
    new: boolean;
  };
  member: {
    center: string;
    difficulty: string;
    max_score: number;
    memberEmail: string;
    memberId: number;
    memberNickname: string;
    member_score: number;
    role: string;
  };
  postContent: string;
  postId: number;
  postThumbnail: string;
  postVideo: string;
  postWriteTime: string;
}
type RootStackParamList = {
  VideoDetail: {id: number};
};


const s3 = new AWS.S3();

const Video = () => {
  const [text, setText] = useState('');
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'VideoDetail'>>();

  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  const onPressVideoDetail = (postId: number) => {
    navigation.navigate("VideoDetail", {id: postId});
  };

  // S3 Bucket 에서 Thumbnail 이미지 불러오기
  useEffect(() => {
    fetchS3Images();
  }, []);

  const [posts, setPosts] = useState<Post[]>([]);
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
    size: 12,
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
      setPosts(response.data.postPage.content);

    } catch (error) { 
      // 요청
      console.error('게시글 목록을 불러오는 데 실패:', error);
    }
  };

  useEffect(() => {
    // 컴포넌트가 마운트될 때 데이터를 불러오기 위해 useEffect를 사용합니다.
    fetchPostList();
  }, []);

  console.log('저장된값 출력하기-----------------------',posts)
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
          data={posts} // posts 배열을 데이터로 설정
          renderItem={({ item }) => {
            // 각 항목의 postThumbnail 값을 S3 URL로 변환
            const imageUrl = `https://drill-video-bucket.s3.ap-northeast-2.amazonaws.com/Thumbnail/${item.postThumbnail}`;
            
            // 이미지를 화면에 표시하는 TouchableOpacity 컴포넌트 반환
            return (
              <TouchableOpacity onPress={() => navigation.navigate("VideoDetail", {id: item.postId})}>
                <Image source={{ uri: imageUrl }} style={{ width: 100, height: 100, margin: 1 }} />
              </TouchableOpacity>
            );
          }}
          keyExtractor={(item) => item.postId.toString()} // 각 항목의 postId를 key로 사용
          numColumns={3}
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