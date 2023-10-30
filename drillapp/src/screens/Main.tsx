import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Image, StyleSheet, Text } from 'react-native';
import { Dropdown } from 'react-native-element-dropdown';
import { RootState } from "../modules/redux/RootReducer";
import { useDispatch, useSelector } from "react-redux";
import axios from 'axios';

type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};


const Main = () => {
  const API_URL = 'http://10.0.2.2:8060/api/ranking/list';
  // 요구하는 매개변수
  const centerName = 'center1';
  const courseName = 'SampleCourse1';
  const [top10Ranks, setTop10Ranks] = useState<string[]>([]);

  // Axios를 사용하여 GET 요청 보내기
  const fetchRankingData = async () => {
    try {
      const response = await axios.get(API_URL, {
        params: {
          centerName: centerName,
          courseName: courseName,
        },
      });

      // 요청 성공
      console.log('랭킹 데이터:', response.data);
      setTop10Ranks(response.data)
    } catch (error) {
      // 요청 실패
      console.error('랭킹 데이터를 불러오는 데 실패', error);
    }
  };

  // 함수 호출
  // fetchRankingData();



  const dispatch = useDispatch()
  // Redux 저장소에서 데이터를 조회
  const userInfo = useSelector((state: RootState) => state.templateUser);

  const [selectedCenter, setSelectedCenter] = useState("지점 선택");
  const [selectedHolder, setSelectedHolder] = useState("홀드");
  const [selectedCourse, setSelectedCourse] = useState("코스");

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
    {key:'difficulty5',value:'하양'},
    {key:'difficulty6',value:'노랑'},
    {key:'difficulty7',value:'주황'},
    {key:'difficulty8',value:'초록'},
  ];

  return (
    <ContainerView>
      <TopView>
        <UserNameView>
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
          </UserNameText>
        </UserNameView>
        <DateView>
          <DateText>Thu, 27 May 2021</DateText>
        </DateView>
        <SelectView>
          <Dropdown 
            style={styles.dropdown1}
            placeholderStyle={styles.placeholderStyle}
            selectedTextStyle={styles.selectedTextStyle}
            inputSearchStyle={styles.inputSearchStyle}
            mode='default'
            data={data}
            maxHeight={200}
            placeholder='지점 선택'
            labelField="value"
            valueField="key"
            value={selectedCenter}
            onChange={(item) => {
              const selectedOption = data.find(option => option.value === item.value);
              setSelectedCenter(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
              fetchRankingData();
            }}
          />
          <Dropdown 
            style={styles.dropdown2}
            placeholderStyle={styles.placeholderStyle}
            selectedTextStyle={styles.selectedTextStyle}
            inputSearchStyle={styles.inputSearchStyle}
            mode='default'
            data={holderColor}
            maxHeight={200}
            placeholder='홀드'
            labelField="value"
            valueField="key"
            value={selectedHolder}
            onChange={(item) => {
              const selectedOption = holderColor.find(option => option.value === item.value);
              setSelectedHolder(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
              fetchRankingData();
            }}
          />
          <Dropdown 
            style={styles.dropdown2}
            placeholderStyle={styles.placeholderStyle}
            selectedTextStyle={styles.selectedTextStyle}
            inputSearchStyle={styles.inputSearchStyle}
            mode='default'
            data={holderColor}
            maxHeight={200}
            placeholder='코스'
            labelField="value"
            valueField="key"
            value={selectedCourse}
            onChange={(item) => {
              const selectedOption = holderColor.find(option => option.value === item.value);
              setSelectedCourse(selectedOption?.key || ''); // 선택된 항목을 찾아 상태 업데이트
              fetchRankingData();
            }}
          />
        </SelectView> 
      </TopView>

      <BottomView>
        <RankingView>
          <RankTitleText>내 순위</RankTitleText>
          <MyRankingText>11위 클라이밍재밌다</MyRankingText>
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
    width: 150,
    height: 40,
    backgroundColor: '#5AC77C',
    borderRadius: 50,
  },
  dropdown2: {
    width: 75,
    height: 40,
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
  font-size: 25px;
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
  flex: 1;
  justify-content: center;
  margin-bottom: 10px;
`
const DateText = styled.Text`
  font-size: 18px;
  text-align: center;
  background-color: white;
  color: black;
`
// -------------------------------

const RankingView = styled.View`
  width: 80%;
  height: 85%;
  background-color: #5AC77C;
  border-radius: 22px;
`
const RankTitleText = styled.Text`
  font-size: 20px;
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
  width: 80%;
  justify-content: center;
  gap: 2px;
`
const Top10RankItem = styled.View`
  flex-direction: row;
  align-items: center;
`
const Top10RankNum = styled.Text`
  flex: 2.5;
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