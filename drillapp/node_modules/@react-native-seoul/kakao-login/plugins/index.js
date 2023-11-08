"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const withAndroidKakaoLogin_1 = require("./android/withAndroidKakaoLogin");
const withIosKakaoLogin_1 = require("./ios/withIosKakaoLogin");
const config_plugins_1 = require("@expo/config-plugins");
const withExpoConfigPlugins = (config, props) => {
    config = (0, withIosKakaoLogin_1.withIosKakaoLogin)(config, props);
    config = (0, withAndroidKakaoLogin_1.withAndroidKakaoLogin)(config, props);
    return config;
};
const pak = require('@react-native-seoul/kakao-login/package.json');
exports.default = (0, config_plugins_1.createRunOncePlugin)(withExpoConfigPlugins, pak.name, pak.version);
