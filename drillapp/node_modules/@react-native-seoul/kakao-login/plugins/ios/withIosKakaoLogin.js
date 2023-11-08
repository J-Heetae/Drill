"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.withIosKakaoLogin = void 0;
/* eslint-disable no-console */
const config_plugins_1 = require("@expo/config-plugins");
const node_fs_1 = require("node:fs");
const KAKAO_SCHEMES = ['kakaokompassauth', 'storykompassauth', 'kakaolink'];
const KAKAO_HEADER_IMPORT_STRING = '#import <RNKakaoLogins.h>';
const KAKAO_LINKING_STRING = `if([RNKakaoLogins isKakaoTalkLoginUrl:url]) {
  return [RNKakaoLogins handleOpenUrl: url];
}`;
const KAKAO_SDK_VERSION_STRING = '$KakaoSDKVersion';
const KAKAO_SDK_VERSION_REGEX = /\$KakaoSDKVersion\=.*(\r\n|\r|\n)/g;
const readFileAsync = async (path) => {
    return new Promise((resolve, reject) => {
        (0, node_fs_1.readFile)(path, 'utf8', (err, data) => {
            if (err || !data) {
                console.error("Couldn't read file:" + path);
                reject(err);
                return;
            }
            resolve(data);
        });
    });
};
const writeFileAsync = async (path, data) => {
    return new Promise((resolve, reject) => {
        (0, node_fs_1.writeFile)(path, data, (err) => {
            if (err) {
                console.error("Couldn't write file:" + path);
                reject(err);
                return;
            }
            resolve();
        });
    });
};
const modifyInfoPlist = (config, props) => {
    return (0, config_plugins_1.withInfoPlist)(config, async (config) => {
        if (!config.modResults.KAKAO_APP_KEY) {
            config.modResults.KAKAO_APP_KEY = props.kakaoAppKey;
        }
        const NEW_URL_TYPES = `kakao${props.kakaoAppKey}`;
        if (!Array.isArray(config.modResults.CFBundleURLTypes)) {
            config.modResults.CFBundleURLTypes = [];
        }
        const urlType = config.modResults.CFBundleURLTypes.find((item) => item.CFBundleURLSchemes.includes(NEW_URL_TYPES));
        if (!urlType) {
            config.modResults.CFBundleURLTypes.push({
                CFBundleURLSchemes: [NEW_URL_TYPES],
            });
        }
        if (!Array.isArray(config.modResults.LSApplicationQueriesSchemes)) {
            config.modResults.LSApplicationQueriesSchemes = KAKAO_SCHEMES;
        }
        else {
            KAKAO_SCHEMES.forEach((scheme) => {
                var _a, _b;
                return !((_a = config.modResults.LSApplicationQueriesSchemes) === null || _a === void 0 ? void 0 : _a.includes(scheme)) &&
                    ((_b = config.modResults.LSApplicationQueriesSchemes) === null || _b === void 0 ? void 0 : _b.push(scheme));
            });
        }
        return config;
    });
};
const modifyAppDelegate = (config) => {
    const modifyContents = (contents) => {
        if (!contents.includes(KAKAO_HEADER_IMPORT_STRING)) {
            contents = contents.replace('#import <React/RCTLinkingManager.h>', '#import <React/RCTLinkingManager.h>\n' + KAKAO_HEADER_IMPORT_STRING);
        }
        if (!contents.includes(KAKAO_LINKING_STRING)) {
            contents = contents.replace('options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {', `options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
      ${KAKAO_LINKING_STRING}`);
        }
        return contents;
    };
    return (0, config_plugins_1.withAppDelegate)(config, (props) => {
        if (['objc', 'objcpp'].includes(props.modResults.language)) {
            props.modResults.contents = modifyContents(props.modResults.contents);
        }
        else {
            config_plugins_1.WarningAggregator.addWarningIOS('withFacebook', 'Swift AppDelegate files are not supported yet.');
        }
        return props;
    });
};
const modifyPodfile = (config, props) => {
    config = (0, config_plugins_1.withXcodeProject)(config, async (_props) => {
        const iosPath = _props.modRequest.platformProjectRoot;
        const podfile = await readFileAsync(`${iosPath}/Podfile`);
        const removedPodfile = podfile.replace(KAKAO_SDK_VERSION_REGEX, '');
        if (props.overrideKakaoSDKVersion) {
            const newPodfile = removedPodfile.concat(`${KAKAO_SDK_VERSION_STRING}="${props.overrideKakaoSDKVersion}"\n`);
            await writeFileAsync(`${iosPath}/Podfile`, newPodfile);
        }
        else {
            await writeFileAsync(`${iosPath}/Podfile`, removedPodfile);
        }
        return _props;
    });
    return config;
};
const withIosKakaoLogin = (config, props) => {
    config = modifyInfoPlist(config, props);
    config = modifyAppDelegate(config);
    config = modifyPodfile(config, props);
    return config;
};
exports.withIosKakaoLogin = withIosKakaoLogin;
