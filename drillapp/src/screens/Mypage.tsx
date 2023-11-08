import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text, Button, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import AsyncStorage from "@react-native-async-storage/async-storage";
import { RootState } from "../modules/redux/RootReducer";
import { useDispatch, useSelector } from "react-redux";
import { StackNavigationProp } from '@react-navigation/stack';
import { useNavigation } from "@react-navigation/native";
import { Dropdown } from 'react-native-element-dropdown';
import axios from 'axios';
import Config from 'react-native-config';

type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};
type RootStackParamList = {
  Login: undefined;
  VideoDetail: {id: number};
};
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
};
interface NextPageDto {
  centerName: string;
  courseName: string;
  difficulty: string;
  memberNickname: string;
  order: string;
  page: number;
  size: number;
};

const Mypage = () => {
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'Login','VideoDetail'>>();
  const [posts, setPosts] = useState<Post[]>([]);
  const [selectedCenter, setSelectedCenter] = useState("center0");
  const [selectedHolder, setSelectedHolder] = useState("difficulty0");
  const [selectedCourse, setSelectedCourse] = useState("course0");
  const [selectedCourseName, setSelectedCourseName] = useState([]);
  const [transformedCourseName, setTransformedCourseName] = useState<{ label: string; value: string; }[]>([]);

  const data: DataItem[] = [
    {key:'center0',value:'전체'},
    {key:'center1',value:'홍대'},
    {key:'center2',value:'일산'},
    {key:'center3',value:'양재'},
    {key:'center4',value:'마곡'},
    {key:'center5',value:'신림'},
    {key:'center6',value:'연남'},
    {key:'center7',value:'강남'},
    {key:'center8',value:'사당'},
    {key:'center9',value:'신사'},
    {key:'center10',value:'서울대'},
  ];
  const holderColor: DataItem[] = [
    {key:'difficulty0',value:'전체'},
    {key:'difficulty1',value:'하양'},
    {key:'difficulty2',value:'노랑'},
    {key:'difficulty3',value:'주황'},
    {key:'difficulty4',value:'초록'},
    {key:'difficulty5',value:'하양'},
    {key:'difficulty6',value:'노랑'},
    {key:'difficulty7',value:'주황'},
    {key:'difficulty8',value:'초록'},
  ];

  const API_URL = `${Config.API_URL}post/list`;
  const [currentPage, setCurrentPage] = useState(0);
  // EntirePostPageDto 객체 생성
  const entirePostPageDto = {
    centerName: selectedCenter,
    courseName: selectedCourse,
    difficulty: selectedHolder,
    memberNickname: userInfo.nickName,
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
      console.log('게시글 가져오는데 성공',response);
      console.log('지점 + 홀드에 대한 코스정보 리스트-----', response.data.courseNameList);
      setSelectedCourseName(response.data.courseNameList);
      console.log('POSTDTO---------', entirePostPageDto);
    } catch (error) { 
      // 요청
      console.error('게시글 목록을 불러오는 데 실패:', error);
    }
  };

  // 무한스크롤
  const endPoint = () => {
    // 현재 페이지 번호를 증가시킵니다.
    setCurrentPage(currentPage + 1);

    // 다음 페이지의 데이터를 불러오기 위한 요청을 보냅니다.
    const nextPageDto = {
      ...entirePostPageDto,
      page: currentPage + 1,
    };
    // 다음 페이지의 데이터를 불러오는 요청 보내기
    fetchNextPageData(nextPageDto);
  };
  const fetchNextPageData = async (nextPageDto: NextPageDto) => {
    try {
      const response = await axios.post(API_URL, nextPageDto, {
        headers: {
          Authorization: userInfo.accessToken,
        },
      });
  
      // 응답 데이터에 postPage가 존재하고 content 프로퍼티도 존재하는지 확인
      if (response.data.postPage && response.data.postPage.content) {
        // 이전 페이지의 데이터와 새로 불러온 데이터를 합칩니다.
        setPosts([...posts, ...response.data.postPage.content]);
        console.log('다음 페이지 데이터 가져오기 성공', response.data);
      } else {
        // 만약 postPage가 없거나 content가 없으면 더 이상 불러올 데이터가 없다고 판단할 수 있습니다.
        console.log('더 이상 불러올 데이터가 없습니다.');
      }
    } catch (error) {
      console.error('다음 페이지 데이터를 불러오는 데 실패:', error);
    }
  };

  useEffect(() => {
    fetchPostList();
  }, []);
  useEffect(() => {
    fetchPostList();
  }, [selectedCenter, selectedCourse, selectedHolder]);
  useEffect(() => {
    const transformed = selectedCourseName.map((course) => ({
      label: course,
      value: course, // or provide unique identifier for value
    }));
    setTransformedCourseName(transformed);
  }, [selectedCourseName]);

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
            {userInfo.nickName}
            <TouchableOpacity onPress={Logout}>
              <Text>로그아웃</Text>
            </TouchableOpacity>
          </UserNameText>
          <UserExpView>

          </UserExpView>
        </UserInfoView>
        <SortMenuView>
          <SortMenuTitle>내 영상 모아보기</SortMenuTitle>
          <MenuView>
            <SortMenu>
              <Dropdown 
                style={styles.dropdown1}
                placeholderStyle={styles.placeholderStyle}
                selectedTextStyle={styles.selectedTextStyle}
                inputSearchStyle={styles.inputSearchStyle}
                mode='default'
                data={data}
                maxHeight={200}
                placeholder="지점"
                labelField="value"
                valueField="key"
                value={selectedCenter}
                onChange={(item) => {
                  const selectedOption = data.find(option => option.value === item.value);
                  setSelectedCenter(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
                }}
              />
            </SortMenu>
            <SortMenu>
              <Dropdown 
                style={styles.dropdown2}
                placeholderStyle={styles.placeholderStyle}
                selectedTextStyle={styles.selectedTextStyle}
                inputSearchStyle={styles.inputSearchStyle}
                mode='default'
                data={holderColor}
                maxHeight={200}
                placeholder="홀드"
                labelField="value"
                valueField="key"
                value={selectedHolder}
                onChange={(item) => {
                  const selectedOption = holderColor.find(option => option.value === item.value);
                  setSelectedHolder(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
                }}
              />
            </SortMenu>
            <SortMenu>
              <Dropdown 
                style={styles.dropdown2}
                placeholderStyle={styles.placeholderStyle}
                selectedTextStyle={styles.selectedTextStyle}
                inputSearchStyle={styles.inputSearchStyle}
                mode='default'
                data={transformedCourseName}
                maxHeight={200}
                placeholder="코스"
                labelField="label" // labelField 설정
                valueField="value" // valueField 설정
                value={selectedCourse}
                onChange={ (item) => {
                  const selectedOption = transformedCourseName.find(option => option.value === item.value);
                  setSelectedCourse(selectedOption?.label || ''); // 선택된 항목을 찾아 상태 업데이트
                }}
              />
            </SortMenu>
          </MenuView>
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
            onEndReached={endPoint}
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
  },
  dropdown1: {
    width: 90,
    height: 30,
    backgroundColor: '#5AC77C',
    borderRadius: 50,
  },
  dropdown2: {
    width: 65,
    height: 30,
    backgroundColor: '#5AC77C',
    borderRadius: 50,
  },
  placeholderStyle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#fff',
  },
  selectedTextStyle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#fff',
  },
  inputSearchStyle: {
    height: 40,
    fontSize: 16,
  },
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
  height: 30px;
  background-color: #5AC77C;
  border-radius: 50px;
  justify-content: center;
  align-items: center;
`
export default Mypage;