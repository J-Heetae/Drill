import { createSlice, PayloadAction } from "@reduxjs/toolkit";

// User에서 관리해야하는 Slice
const initialState = {
    name: '',
    nickName: '',
    place: '',
    email: '',
    accessToken: '',
    refreshToken: '',
};

/**
 * TemplateSlice에서 관리할 상태를 지정
 */
export const TemplateUserSlice = createSlice({
    name: 'templateUser',
    initialState,
    reducers: {
        // 모든 사용자 정보를 상태에 저장
        setUser(state, action) {
            state.name = action.payload.name;
            state.nickName = action.payload.nickName;
            state.place = action.payload.place;
            state.email = action.payload.email;
            state.accessToken = action.payload.accessToken;
        },

        // 사용자 이름을 상태에 저장
        setName(state, action) {
            state.name = action.payload;
        },

        // 닉네임을 상태에 저장
        setNickName(state, action) {
            state.nickName = action.payload;
        },

        // 자주가는 지점을 상태에 저장
        setPlace(state, action) {
            state.place = action.payload;
        },

        // 사용자 이메일을 상태에 저장
        setEmail(state, action) {
            state.email = action.payload;
        },

        // 접근 토큰을 상태에 저장
        setAccessToken(state, action) {
            state.accessToken = action.payload;
        },

        // 접근 토큰을 상태에 저장
        setRefreshToken(state, action) {
            state.refreshToken = action.payload;
        },

    },
});

export const { setUser, setName, setNickName, setPlace, setEmail, setAccessToken, setRefreshToken } = TemplateUserSlice.actions

export default TemplateUserSlice.reducer