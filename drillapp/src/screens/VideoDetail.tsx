import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { TextInput, StyleSheet, Image, Text, Button } from 'react-native';
import {RouteProp, useRoute} from '@react-navigation/native';
import { RootState } from "../modules/redux/RootReducer";
import { useSelector } from "react-redux";
import axios
 from 'axios';
type RootStackParamList = {
  VideoDetail: {id: number};
};
interface Detail {
  memberNickname: string;
  centerName: number;
  postContent: string;
  postVideo: string;
  postWriteTime: string;
  courseName: string;
  likedCount: number;
  commentCount: number;
}

const VideoDetail = () => {
  type ScreenRouteProp = RouteProp<RootStackParamList,"VideoDetail">;
  const route = useRoute<ScreenRouteProp>();
  const userInfo = useSelector((state: RootState) => state.templateUser);

  const [data, setData] = useState<Detail | null>(null);

  const API_URL = `http://10.0.2.2:8060/api/post/${route.params?.id}`;
  const VideoDetailget = async () => {
    try {
      const response = await axios.get(API_URL, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 성공
      console.log('상세 게시글 불러오는데 성공', response.data)
      setData(response.data)
    } catch (error) { 
      // 요청
      console.error('게시글 목록을 불러오는 데 실패:', error);
    }
  };

  useEffect(() => {
    // 컴포넌트가 마운트될 때 데이터를 불러오기 위해 useEffect를 사용합니다.
    VideoDetailget();
  }, []);

  return (
    <ContainerView>
      <TopView>
        <UserNameView>
          <UserNameText>{data?.memberNickname}</UserNameText>
        </UserNameView>
        <UserVideoView>
      
        </UserVideoView>
      </TopView>

      <BottomView>
        <PostTopView>
          <PostLikedView>
            <Text>좋아요!!</Text>
          </PostLikedView>
          <PostContentView>
            <Text>{data?.postContent}</Text>
          </PostContentView>
        </PostTopView>
        <PostBottomView>
          <Text>
            댓글
          </Text>
        </PostBottomView>
      </BottomView>
    </ContainerView>
  );
};

const styles = StyleSheet.create({
  
});

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
`
// -------------------------------

const TopView = styled.View`
  flex: 1;
`;
const BottomView = styled.View`
  flex: 1;
`;
// -------------------------------

const UserNameView = styled.View`
  flex: 1;
  justify-content: center;
`
const UserNameText = styled.Text`
  fontSize: 20px;
  font-weight: 900;
  color: black;
  margin-left: 10px;
`
const UserVideoView = styled.View`
  flex: 5;
  background-color: black;
`

// -------------------------------
const PostTopView = styled.View`
  flex: 1;
`
const PostBottomView = styled.View`
  flex: 1.2;
  background-color: gray;
`
const PostLikedView = styled.View`
  flex: 1;
  background-color: blue;
`
const PostContentView = styled.View`
  flex: 1.5;
`
export default VideoDetail;