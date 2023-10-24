import React, { FC, useState } from 'react';
import styled from 'styled-components/native';
import { Dimensions, Image } from 'react-native';
import { SelectList } from 'react-native-dropdown-select-list'

type DataItem = {
  key: string;
  value: string;
  disabled?: boolean;
};


const Main = () => {
  const [selected, setSelected] = useState<string>("");
  const windowHeight = Dimensions.get('window').height;
  const data: DataItem[] = [
    {key:'1',value:'더클라임 홍대'},
    {key:'2',value:'더클라임 일산'},
    {key:'3',value:'더클라임 양재'},
    {key:'4',value:'더클라임 마곡'},
    {key:'5',value:'더클라임 신림'},
    {key:'6',value:'더클라임 연남'},
    {key:'7',value:'더클라임 강남'},
    {key:'8',value:'더클라임 사당'},
    {key:'9',value:'더클라임 신사'},
    {key:'10',value:'더클라임 서울대'},
  ];
  const holderColor: DataItem[] = [
    {key:'1',value:'하양'},
    {key:'2',value:'노랑'},
    {key:'3',value:'주황'},
    {key:'4',value:'초록'},
    {key:'5',value:'하양'},
    {key:'6',value:'노랑'},
    {key:'7',value:'주황'},
    {key:'8',value:'초록'},
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
            클라이밍재밌다
          </UserNameText>
        </UserNameView>
        <DateView>
          <DateText>Thu, 27 May 2021</DateText>
        </DateView>
        <SelectView>
          <SelectList 
            onSelect={() => (selected)}
            setSelected={setSelected} 
            data={data}  
            save="value"
            search={false} 
            placeholder="지점명"
            inputStyles={{ color: 'white', fontSize: 15}}
            dropdownTextStyles={{color: 'white', fontSize: 15}}
            boxStyles={{ width: 200, height: 50, backgroundColor: '#5AC77C', borderColor: '#5AC77C', borderRadius: 50 }}
            dropdownStyles={{ width: 200, height: Math.min(windowHeight - 100, 200), backgroundColor: '#5AC77C' }}    
          />
          <SelectList 
            onSelect={() => (selected)} 
            setSelected={setSelected} 
            data={holderColor}  
            save="value"
            search={false} 
            placeholder="홀드 색"
            inputStyles={{ color: 'white', fontSize: 15}}
            dropdownTextStyles={{color: 'white', fontSize: 15}}
            boxStyles={{ width: 100, height: 50, backgroundColor: '#5AC77C', borderColor: '#5AC77C', borderRadius: 50 }}
            dropdownStyles={{ width: 100, height: Math.min(windowHeight - 100, 200), backgroundColor: '#5AC77C' }}
          />
        </SelectView> 
      </TopView>

      <BottomView>
        <RankingView>
          <RankTitleText>내 순위</RankTitleText>
          <MyRankingText>11위 클라이밍재밌다</MyRankingText>
          <Top10RankText>

          </Top10RankText>
        </RankingView>
      </BottomView>
    </ContainerView>
  )
};

const ContainerView = styled.View`
  flex: 1;
  background-color: white;
`
// -------------------------------

const TopView = styled.View`
  flex: 1;
  zIndex: 2;
`;
const BottomView = styled.View`
  flex: 2.5;
  justify-content: center;
  align-items: center;
  zIndex: 1;
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
  gap: 10px;
  zIndex: 999;
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
const Top10RankText = styled.Text`
  font-size: 15px;
  text-align: center;
`
export default Main;