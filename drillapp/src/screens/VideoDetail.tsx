import React, { useEffect, useState } from 'react';
import styled from 'styled-components/native';
import { TextInput, StyleSheet, Image, Text, Button } from 'react-native';




const VideoDetail = () => {
  
  return (
    <ContainerView>
      <TopView>
        게시글 상세 페이지
      </TopView>

      <BottomView>

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
  }
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

export default VideoDetail;