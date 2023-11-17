import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { FlatList, TextInput, StyleSheet, Image, Text, TouchableOpacity, Platform } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import axios from 'axios';
import { RootState } from "../modules/redux/RootReducer";
import { useSelector } from "react-redux";
import AWS from 'aws-sdk';
import { useNavigation } from "@react-navigation/native";
import { StackNavigationProp } from '@react-navigation/stack';
import { Dropdown } from 'react-native-element-dropdown';
import { API_URL_Local } from "@env";

AWS.config.update({
  accessKeyId: 'AKIA32XVP6DS7XK33DGC',
  secretAccessKey: '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',
  region: 'ap-northeast-2',
});
type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
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
}
interface NextPageDto {
  centerName: string;
  courseName: string;
  difficulty: string;
  memberNickname: string;
  order: string;
  page: number;
  size: number;
}
type RootStackParamList = {
  VideoDetail: {id: number};
};


const s3 = new AWS.S3();

const Video = () => {
  const [text, setText] = useState('');
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'VideoDetail'>>();

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
    {key:'difficulty1',value:'하양'},
    {key:'difficulty2',value:'노랑'},
    {key:'difficulty3',value:'주황'},
    {key:'difficulty4',value:'초록'},
    {key:'difficulty5',value:'파랑'},
    {key:'difficulty6',value:'빨강'},
    {key:'difficulty7',value:'보라'},
    {key:'difficulty8',value:'핑크'},
    {key:'difficulty9',value:'검정'},
  ];


  const onChangeText = (inputText: string) => {
    setText(inputText);
    handleSearch;
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

  const API_URL = `${API_URL_Local}post/list`;
  const [currentPage, setCurrentPage] = useState(0);
  // EntirePostPageDto 객체 생성
  const entirePostPageDto = {
    centerName: selectedCenter,
    courseName: selectedCourse,
    difficulty: selectedHolder,
    memberNickname: text,
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

  // 검색 기능
  const searchPosts = async () => {
    try {
      const searchDto = {
        ...entirePostPageDto,
        memberNickname: text, // 사용자가 입력한 검색어를 memberNickname에 넣기
      };

      const response = await axios.post(API_URL, searchDto, {
        headers: {
          Authorization: userInfo.accessToken,
        },
      });

      setPosts(response.data.postPage.content);
      console.log('검색 결과:', response.data);
    } catch (error) {
      console.error('게시글 검색 실패:', error);
    }
  };  
  const handleSearch = () => {
    // 검색 버튼을 누르면 검색 요청을 보내기
    searchPosts();
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
    // 컴포넌트가 마운트될 때 데이터를 불러오기 위해 useEffect를 사용합니다.
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

  console.log('저장된값 출력하기-----------------------',posts)
  return(
    <ContainerView>
        <TopView>
          <SearchView>
            <TextInput
              onChangeText={onChangeText}
              value={text}
              placeholder='유저 검색'
              placeholderTextColor='black'
              style={styles.input}
            />
            <TouchableOpacity onPress={handleSearch} style={{width:40, height:50, paddingTop:7, justifyContent:'center', alignItems:'center'}}>
              <Text style={{color:'black'}}>검색</Text>
            </TouchableOpacity>
          </SearchView>
          <SortMenuView>
            <Dropdown 
              style={styles.dropdown1}
              placeholderStyle={styles.placeholderStyle}
              selectedTextStyle={styles.selectedTextStyle}
              inputSearchStyle={styles.inputSearchStyle}
              itemTextStyle={styles.itemTextStyle}
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

            <Dropdown 
              style={styles.dropdown2}
              placeholderStyle={styles.placeholderStyle}
              selectedTextStyle={styles.selectedTextStyle}
              inputSearchStyle={styles.inputSearchStyle}
              itemTextStyle={styles.itemTextStyle}
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

            <Dropdown 
              style={styles.dropdown2}
              placeholderStyle={styles.placeholderStyle}
              selectedTextStyle={styles.selectedTextStyle}
              inputSearchStyle={styles.inputSearchStyle}
              itemTextStyle={styles.itemTextStyle}
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
                    <Image source={{ uri: imageUrl }} style={{ width: 136, height: 136, margin: 1 }} />
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
  input: {
    height: '50%',
    width: '75%',
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 15,
    paddingHorizontal: 8,
    marginTop: 30,
    marginBottom: 20,
    fontSize:20,
    color: 'black',
  },
  list: {
    width: '100%',
  },
  item: {
    aspectRatio: 1,
    width: '100%',
    flex: 1,
  },
  dropdown1: {
    width: '35%',
    height: '80%',
    borderWidth: 1,
    borderRadius: 10,
    borderColor: '#5AC77C',
    backgroundColor: '#5AC77C',
    ...Platform.select({
      android: {
        elevation: 5,
      },
    }),
  },
  dropdown2: {
    width: '22%',
    height: '80%',
    borderWidth: 1,
    borderRadius: 10,
    borderColor: '#5AC77C',
    backgroundColor: '#5AC77C',
    ...Platform.select({
      android: {
        elevation: 5,
      },
    }),
  },
  placeholderStyle: {
    fontSize: 20,
    textAlign: 'center',
    color: '#fff',
  },
  selectedTextStyle: {
    fontSize: 20,
    textAlign: 'center',
    color: '#fff',
  },
  inputSearchStyle: {
    height: '90%',
    fontSize: 20,
    color: '#000',
  },
  itemTextStyle: {
    color: '#000'
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
const SearchView = styled.View`
  flex: 2;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 5px;
`
const SortMenuView = styled.View`
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
  gap: 8px;
`

export default Video;