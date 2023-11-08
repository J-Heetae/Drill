import { createSlice, PayloadAction } from "@reduxjs/toolkit";

// User에서 관리해야하는 Slice
const initialState = {
    tempId: '',
    tempName: '',
    tempMenu: '',
};

/**
 * TemplateSlice에서 관리할 상태를 지정
 */
export const TemplateSlice = createSlice({
    name: 'template',
    initialState,
    reducers: {

        // 모든 사용자 정보를 상태에 저장
        setTemplate(state, action) {
            state.tempId = action.payload.tempId;
            state.tempName = action.payload.tempName;
            state.tempMenu = action.payload.tempMenu;
        },

        setTempId(state, action) {
            state.tempId = action.payload;
        },

        setTempName(state, action) {
            state.tempName = action.payload;
        },

        setTempMenu(state, action) {
            state.tempMenu = action.payload;
        },

    },
});

export const { setTemplate, setTempId, setTempName, setTempMenu } = TemplateSlice.actions

export default TemplateSlice.reducer