import React, {useEffect, useState, useRef} from 'react';
import styled from 'styled-components/native';
import {
  TextInput,
  StyleSheet,
  Image,
  Text,
  Button,
  View,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import {RouteProp, useRoute} from '@react-navigation/native';
import {RootState} from '../modules/redux/RootReducer';
import {useSelector} from 'react-redux';
import Video from 'react-native-video';
import VideoRef from 'react-native-video';
import axios from 'axios';
import AWS from 'aws-sdk';
import {isBuffer, isError} from 'lodash';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view';
import {API_URL_Local} from '@env';

AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});
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
  liked: boolean;
  likedCount: number;
  commentCount: number;
}
interface Comment {
  commentContent: string;
  commenetCount: number;
  commentWriteTime: string;
  memberNickname: string;
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
  post: {
    center: string;
    course: object;
    member: object;
    postContent: string;
    postId: number;
    postThumbnail: string;
    postVideo: string;
    postWriteTime: string;
  };
}

const VideoDetail = () => {
  type ScreenRouteProp = RouteProp<RootStackParamList, 'VideoDetail'>;
  const route = useRoute<ScreenRouteProp>();
  const userInfo = useSelector((state: RootState) => state.templateUser);

  const [data, setData] = useState<Detail | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const handleVideoLoad = () => {
    // 비디오 로딩이 완료되면 로딩 상태를 false로 업데이트
    setIsLoading(false);
  };
  const videoRef = useRef<VideoRef>(null);
  const background = "https://drill-video-bucket.s3.ap-northeast-2.amazonaws.com/Video/"
  
  const API_URL1 = `${API_URL_Local}post/read`;
  const API_URL2 = `${API_URL_Local}comment/`;
  const API_URL3 = `${API_URL_Local}comment/list/${route.params?.id}`;
  const API_URL4 = `${API_URL_Local}liked`;

  const [isLiked, setIsLiked] = useState(false); // 좋아요 상태를 저장하는 상태 변수
  const [countLiked, setCountLiked] = useState<number>(5);

  const likedDto = {
    memberNickname: userInfo.nickName,
    postId: route.params?.id,
  };
  // 좋아요 버튼을 눌렀을 때 호출되는 함수
  const handleLikeButtonPress = async () => {
    try {
      const response = await axios.post(API_URL4, likedDto, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 성공
      console.log('게시글 좋아요 성공', response);
      setIsLiked(!isLiked); // 좋아요 상태를 토글(toggle)
      // 좋아요 버튼을 누를 때, isLiked 값에 따라 좋아요 개수 업데이트
      setCountLiked(isLiked ? countLiked - 1 : countLiked + 1);
    } catch (error) {
      // 요청
      console.error('게시글 좋아요 실패:', error);
    }
  };

  const [text, setText] = useState('');
  const onChangeText = (inputText: string) => {
    setText(inputText);
  };

  const readDto = {
    memberNickname: userInfo.nickName,
    postId: route.params?.id,
  };
  const VideoDetailget = async () => {
    try {
      const response = await axios.post(API_URL1, readDto, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 성공
      console.log('상세 게시글 불러오는데 성공', response.data);
      setData(response.data);
      setIsLiked(response.data?.liked || false);
      setCountLiked(response.data.likedCount);
    } catch (error) {
      // 요청
      console.error('게시글 목록을 불러오는 데 실패:', error);
    }
  };

  const commentDto = {
    commentContent: text,
    memberNickname: userInfo.nickName,
    postId: route.params?.id,
  };
  const Commentpost = async () => {
    try {
      const response = await axios.post(API_URL2, commentDto, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 성공
      console.log('댓글 작성 성공', response);
      setText(''); // 입력창 초기화

      Commentpostget();
    } catch (error) {
      // 요청
      console.error('댓글 작성 실패:', error);
    }
  };

  const [comments, setComments] = useState<Comment[]>([]);
  const Commentpostget = async () => {
    try {
      const response = await axios.get(API_URL3, {
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });
      // 성공
      console.log('댓글 목록 불러오기 성공', response.data);
      setComments(response.data.reverse());
    } catch (error) {
      // 요청
      console.error('댓글 목록 불러오기 실패:', error);
    }
  };

  useEffect(() => {
    // 컴포넌트가 마운트될 때 로딩 화면을 표시하기 위해 setTimeout을 사용한 가상의 로딩 시간 설정
    const loadingTimeout = setTimeout(() => {
      setIsLoading(false);
    }, 5000); // 5초간 로딩 화면을 표시

    return () => {
      // 컴포넌트가 언마운트될 때 clearTimeout으로 타이머를 제거하여 메모리 누수 방지
      clearTimeout(loadingTimeout);
    };
  }, []);

  useEffect(() => {
    // 컴포넌트가 마운트될 때 데이터를 불러오기 위해 useEffect를 사용합니다.
    VideoDetailget();
    Commentpostget();

    console.log('현재 유저가 눌러서 좋아요 형식--------', isLiked);
  }, []);

  return (
    <ContainerView>
      <TopView>
        <UserNameView>
          <UserNameText>{data?.memberNickname}</UserNameText>
        </UserNameView>
        <UserVideoView
          style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
          {isLoading && (
            <Text style={{color: 'black', fontSize: 20}}>
              잠시 기다려 주세요
            </Text>
          )}
          <Video
            // Can be a URL or a local file.
            source={{uri: background+data?.postVideo}}
            // Store reference  
            ref={videoRef}
            controls={true}
            paused={false}
            resizeMode={'cover'}
            // Callback when remote video is buffering
            onBuffer={isBuffer}
            // Callback when video cannot be loaded
            onError={isError}
            style={styles.backgroundVideo}
          />
        </UserVideoView>
      </TopView>

      <BottomView>
        <PostTopView>
          <PostLikedView>
            <TouchableOpacity onPress={handleLikeButtonPress}>
              <View>
                <Image
                  source={
                    isLiked
                      ? require('drillapp/src/asset/icons/Heart_Active.png')
                      : require('drillapp/src/asset/icons/Heart.png')
                  }
                  resizeMode="contain"
                  style={{
                    width: 25,
                    height: 25,
                  }}
                />
              </View>
            </TouchableOpacity>
            <Text style={{fontSize: 20, fontWeight: 'bold', color: 'black'}}>
              좋아요 {countLiked}개
            </Text>
          </PostLikedView>
          <PostContentView>
            <Text style={{fontSize: 22, fontWeight: 'bold', color: 'black'}}>
              {data?.memberNickname}
            </Text>
            <Text style={{fontSize: 20, color: 'black'}} numberOfLines={2}>
              {data?.postContent}
            </Text>
          </PostContentView>
        </PostTopView>
        <PostBottomView>
          <PostBottomSearch>
            <TextInput
              onChangeText={onChangeText}
              value={text}
              placeholder="댓글 달기"
              placeholderTextColor="black"
              style={styles.input}
            />
            <TouchableOpacity onPress={Commentpost}>
              <Text style={{color: 'black'}}>게시</Text>
            </TouchableOpacity>
          </PostBottomSearch>
          <PostBottomComment>
            <ScrollView>
              {comments.map((comment, index) => (
                <View
                  key={index}
                  style={{display: 'flex', flexDirection: 'row', gap: 10}}>
                  <View>
                    <Text
                      style={{
                        fontSize: 22,
                        fontWeight: 'bold',
                        color: 'black',
                      }}>
                      {comment.memberNickname}
                    </Text>
                  </View>
                  <View>
                    <Text style={{fontSize: 20, color: 'black'}}>
                      {comment.commentContent}
                    </Text>
                  </View>
                </View>
              ))}
            </ScrollView>
          </PostBottomComment>
        </PostBottomView>
      </BottomView>
    </ContainerView>
  );
};

const styles = StyleSheet.create({
  backgroundVideo: {
    position: 'absolute',
    top: 0,
    left: 0,
    bottom: 0,
    right: 0,
  },
  input: {
    height: '85%',
    width: '80%',
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 15,
    paddingHorizontal: 8,
    fontSize: 15,
  },
});

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
`;
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
`;
const UserNameText = styled.Text`
  font-size: 25px;
  font-weight: 900;
  color: black;
  margin-left: 10px;
`;
const UserVideoView = styled.View`
  flex: 5;
`;

// -------------------------------
const PostTopView = styled.View`
  flex: 1;
`;
const PostBottomView = styled.View`
  flex: 1.2;
  padding-left: 20px;
`;
const PostLikedView = styled.View`
  flex: 0.5;
  align-items: center;
  display: flex;
  flex-direction: row;
  gap: 10px;
  padding-left: 20px;
`;
const PostContentView = styled.View`
  flex: 1;
  display: flex;
  padding-left: 20px;
  padding-right: 10px;
`;
// -------------------------------
const PostBottomSearch = styled.View`
  flex: 1;
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
`;
const PostBottomComment = styled.View`
  flex: 2;
`;

export default VideoDetail;