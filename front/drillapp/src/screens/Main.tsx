import React, { useState, useEffect } from 'react';
import styled from 'styled-components/native';
import { Image, StyleSheet, Text, Platform } from 'react-native';
import { Dropdown } from 'react-native-element-dropdown';
import { RootState } from "../modules/redux/RootReducer";
import { useDispatch, useSelector } from "react-redux";
import axios from 'axios';
import { API_URL_Local } from "@env";

type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};


const Main = () => {
  const userInfo = useSelector((state: RootState) => state.templateUser);
  const API_URL = `${API_URL_Local}ranking/first`;
  const API_URL2 = `${API_URL_Local}ranking/list`;
  const API_URL3 = `${API_URL_Local}ranking/myranking`;
  // 요구하는 매개변수
  const centerName = userInfo.place;
  // const courseName = 'difficulty1Course1';
  const [top10Ranks, setTop10Ranks] = useState<string[]>([]);
  


  let today = new Date(); // today 객체에 Date()의 결과를 넣어줬다
  let time = {
    year: today.getFullYear(),  //현재 년도
    month: today.getMonth() + 1, // 현재 월
    date: today.getDate(), // 현제 날짜
    hours: today.getHours(), //현재 시간
    minutes: today.getMinutes(), //현재 분
  };
  let currentDate = `${time.year}년 ${time.month}월 ${time.date}일`

  const myRankingData = async() => {
    const myRankingDto = {
      memberNickname: userInfo.nickName,
      courseName: selectedCourse,
    };

    try{
      const response = await axios.post(API_URL3, myRankingDto, {
        headers: {
          Authorization: userInfo.accessToken, 
        },
      });
      const myRankingValue: number = response.data; 
      if (myRankingValue > 0 ) {
        setMyRanking(myRankingValue);
      } else {
        setMyRanking(0);
      }
      console.log(myRankingValue)
    }
    catch (error) {
      console.log('랭킹 dto', myRankingDto)
      console.error('내 랭킹 불러오는 데 실패', error);
    }
  };
  // Axios를 사용하여 GET 요청 보내기
  const fetchRankingData = async () => {
    try {
      console.log(centerName+" "+selectedCourse);
      const response = await axios.get(API_URL2, {
        params: {
          centerName: selectedCenter,
          courseName: selectedCourse,
        },
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });

      // 요청 성공
      console.log('랭킹 데이터:', response.data);
      setTop10Ranks(response.data);
    } catch (error) {
      // 요청 실패
      console.error('랭킹 데이터를 불러오는 데 실패', error);
    }
  };

  const giveMeDifficulty = async () => {
    try {
      console.log("설마 님이");
      const response = await axios.get('https://k9a106.p.ssafy.io/api/ranking/first', {
        params: {
          centerName: selectedCenter,
        },
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });

      // 요청 성공
      // console.log('랭킹 데이터: '+ response.data.courseName+' 최종');
      setSelectedCourseName(response.data.courseName);
    } catch (error) {
      // 요청 실패
      console.error('랭킹 데이터를 불러오는 데 실패', error);
    }
  };

  const giveMeCoureName = async () => {
    try {
      const response = await axios.get('https://k9a106.p.ssafy.io/api/ranking/findCourseName', {
        params: {
          centerName: selectedCenter,
          difficulty: selectedHolder,
        },
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });

      // 요청 성공
      console.log('랭킹 데이터:', response.data);
      setSelectedCourseName(response.data);
    } catch (error) {
      // 요청 실패
      console.error('랭킹 데이터를 불러오는 데 실패', error);
    }
  };

  // 함수 호출
  // fetchRankingData();



  const dispatch = useDispatch()

  const [selectedCenter, setSelectedCenter] = useState(userInfo.place);
  const [selectedHolder, setSelectedHolder] = useState("difficulty1");
  const [selectedCourse, setSelectedCourse] = useState("");
  const [myRanking, setMyRanking] = useState(0);
  const [selectedCourseName, setSelectedCourseName] = useState([]);
  const [transformedCourseName, setTransformedCourseName] = useState<{ label: string; value: string; }[]>([]);
  const [memberL, setMemberL] = useState(require("../asset/icons/difficulty1.png"));

  

  const data: DataItem[] = [
    {key:'center1',value:'더클라임 홍대'},
    {key:'center2',value:'더클라임 일산'},
    {key:'center3',value:'더클라임 양재'},
    {key:'center4',value:'더클라임 마곡'},
    {key:'center5',value:'더클라임 신림'},
    {key:'center6',value:'더클라임 연남'},
    {key:'center7',value:'더클라임 강남'},
    {key:'center8',value:'더클라임 사당'},
    {key:'center9',value:'더클라임 신사'},
    {key:'center10',value:'더클라임 서울대'},
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

  function findValueByKey(key: string, data: DataItem[]): string | undefined {
    const foundItem = data.find(item => item.key === key);
    return foundItem?.value; // 해당하는 key의 value를 반환하거나, 해당하는 값이 없으면 undefined를 반환합니다.
  }
    

  const [defaultCenter, setDefaultCenter] = useState(userInfo.place);

  const giveLevel = async () => {
    console.log("go?")
    try {
        const response = await axios.get('https://k9a106.p.ssafy.io/api/member/mypage', {
        params: {
          memberNickname: userInfo.nickName,
        },
        headers: {
          Authorization: userInfo.accessToken, // accessToken을 헤더에 추가
        },
      });

      // 요청 성공
      console.log('랭킹 데이터:', response.data);
      setMemberL(response.data.difficulty);
    } catch (error) {
      // 요청 실패
      console.error('유저 데이터를 불러오는 데 실패', error);
    }
  };

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        console.log(centerName);
        const response = await axios.get(API_URL, {
          params: {
            centerName: centerName,
          },
          headers: {
            Authorization: userInfo.accessToken,
          },
        });
  
        console.log('랭킹 데이터:', response.data);
        setTop10Ranks(response.data.ranking);
        setSelectedCourseName(response.data.courseName);
        // console.log(response.data.courseName[0]);

        

        const foundCenter = findValueByKey(centerName, data);
        if (foundCenter) {
          setDefaultCenter(foundCenter);
        }
        setSelectedCourse(response.data.courseName[0]);
        setSelectedHolder(response.data.difficulty[0]);
      } catch (error) {
        console.error('랭킹 데이터를 불러오는 데 실패', error);
      }
    };

    fetchInitialData(); // 페이지가 처음 로딩될 때 데이터를 가져옴
    myRankingData();
    giveLevel();

  }, []); // 빈 배열을 두 번째 인자로 전달하여, 컴포넌트가 처음으로 렌더링될 때만 실행되도록 함
 
  useEffect(() => {
    const transformed = selectedCourseName.map((course) => ({
      label: course,
      value: course, // or provide unique identifier for value
    }));
    setTransformedCourseName(transformed);
    console.log("tlfgod?")
  }, [selectedCourseName]); 



  useEffect(() => {
    fetchRankingData();
    myRankingData();
  }, [selectedCourse]);

  useEffect(() => {
    giveMeDifficulty();
    // console.log(selectedCenter);
  }, [selectedCenter]);

  useEffect(() => {
    giveMeCoureName();
  }, [selectedHolder]); 

  return (
    <ContainerView>
      <TopView>
        <UserNameView>
          <UserNameText>
            <Image
              source={
                memberL === "difficulty1"
                  ? require("../asset/icons/difficulty1.png")
                  : memberL === "difficulty2"
                  ? require("../asset/icons/difficulty2.png")
                  : memberL === "difficulty3"
                  ? require("../asset/icons/difficulty3.png")
                  : memberL === "difficulty3"
                  ? require("../asset/icons/difficulty3.png")
                  : memberL === "difficulty4"
                  ? require("../asset/icons/difficulty4.png")
                  : memberL === "difficulty5"
                  ? require("../asset/icons/difficulty5.png")
                  : memberL === "difficulty6"
                  ? require("../asset/icons/difficulty6.png")
                  : memberL === "difficulty7"
                  ? require("../asset/icons/difficulty7.png")
                  : memberL === "difficulty8"
                  ? require("../asset/icons/difficulty8.png")
                  : memberL === "difficulty9"
                  ? require("../asset/icons/difficulty9.png")
                  : memberL === "difficulty10"
                  ? require("../asset/icons/difficulty10.png")
                  : require("../asset/icons/Profile.png")
              }
              resizeMode="contain"
              style={{
                width: 50,
                height: 50,
              }}
            />
            {userInfo.nickName}
          </UserNameText>
        </UserNameView>
        <DateView>
          <DateText>{currentDate}</DateText>
        </DateView>
        <SelectView>
          <Dropdown 
            style={styles.dropdown1}
            placeholderStyle={styles.placeholderStyle}
            selectedTextStyle={styles.selectedTextStyle}
            inputSearchStyle={styles.inputSearchStyle}
            itemTextStyle={styles.itemTextStyle}
            mode='default'
            data={data}
            maxHeight={200}
            placeholder={defaultCenter}
            labelField="value"
            valueField="key"
            value={selectedCenter}
            onChange={(item) => {
              const selectedOption = data.find(option => option.value === item.value);
              setSelectedCenter(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
              // fetchRankingData();
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
            placeholder={selectedHolder}
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
            placeholder={selectedCourse}
            labelField="label" // labelField 설정
            valueField="value" // valueField 설정
            value={selectedCourse}
            onChange={ (item) => {
              const selectedOption = transformedCourseName.find(option => option.value === item.value);
              setSelectedCourse(selectedOption?.label || ''); // 선택된 항목을 찾아 상태 업데이트
              
            }}
          />
        </SelectView> 
      </TopView>

      <BottomView>
        <RankingView>
          <RankTitleText>{myRanking !== null ? (
    myRanking > 0 ? (
      `내 순위: ${myRanking}위`
    ) : (
      '순위가 없습니다.'
    )
  ) : (
    '로딩 중...'
  )}</RankTitleText>
          <Top10RankView>
            {top10Ranks.map((rank, index) => (
              <Top10RankItem key={index}>
                <Top10RankNum>{index + 1}위</Top10RankNum>
                <Top10RankNickname>{rank}</Top10RankNickname>
              </Top10RankItem>
            ))}
          </Top10RankView>
        </RankingView>
      </BottomView>
    </ContainerView>
  )
};

const styles = StyleSheet.create({
  dropdown1: {
    width: '35%',
    height: '90%',
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
    height: '90%',
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
  z-index: 1;
`;
const BottomView = styled.View`
  flex: 2.5;
  justify-content: center;
  align-items: center;
`;
// -------------------------------

const UserNameView = styled.View`
  flex: 4;
  justify-content: center;
`
const UserNameText = styled.Text`
  font-size: 40px;
  text-align: center;
  font-weight: 900;
  color: black;
`
const SelectView = styled.View`
  flex: 2;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 5px;
  
`
const DateView = styled.View`
  flex: 1.3;
  justify-content: center;
  margin-bottom: 10px;
`
const DateText = styled.Text`
  font-size: 25px;
  text-align: center;
  background-color: white;
  color: black;
`
// -------------------------------

const RankingView = styled.View`
  width: 83%;
  height: 85%;
  border: 1px solid #ADA4A5;
  border-radius: 22px;
  background-color: #5AC77C;
  ${Platform.OS === 'android'
    ? 'elevation: 5;'
    : 'shadowColor: #000; shadowOffset: 0 2px; shadowOpacity: 0.2; shadowRadius: 5px;'}
`
const RankTitleText = styled.Text`
  font-size: 35px;
  text-align: center;
  color: white;
  margin-top: 10px;
  margin-bottom: 10px;
`
const MyRankingText = styled.Text`
  font-size: 30px;
  font-weight: bold;
  text-align: center;
  color: white;
`
const Top10RankView = styled.View`
  flex: 1;
  width: 100%;
  justify-content: center;
  gap: 2px;
`
const Top10RankItem = styled.View`
  flex-direction: row;
  align-items: center;
`
const Top10RankNum = styled.Text`
  flex: 1.5;
  text-align: center;
  font-size: 20px;
  color: white;
`
const Top10RankNickname = styled.Text`
  flex: 2;
  text-align: center;
  font-size: 20px;
  color: white;
`
export default Main;