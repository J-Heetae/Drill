import React, { useState } from 'react';
import styled from 'styled-components/native';
import { Calendar, CalendarList, Agenda } from 'react-native-calendars';

const CalendarPage = () => {
  const [selected, setSelected] = useState('');

  return (
    <Calendar
      onDayPress={day => {
        setSelected(day.dateString);
      }}
      markedDates={{
        [selected]: {selected: true, disableTouchEvent: true, selectedColor: 'green'}
      }}
    />
  );
};

const StyledText = styled.Text`
  font-size: 30px;
`;

export default CalendarPage;