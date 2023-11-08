"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.withAndroidKakaoLogin = void 0;
const config_plugins_1 = require("@expo/config-plugins");
const ACTIVITY_NAME = 'com.kakao.sdk.auth.AuthCodeHandlerActivity';
const modifyAndroidManifest = (config, props) => {
    return (0, config_plugins_1.withAndroidManifest)(config, (config) => {
        const mainApplication = config_plugins_1.AndroidConfig.Manifest.getMainApplicationOrThrow(config.modResults);
        const newPreviewActivity = {
            $: {
                'android:name': ACTIVITY_NAME,
                'android:exported': 'true',
            },
            'intent-filter': [
                {
                    action: [
                        {
                            $: {
                                'android:name': 'android.intent.action.VIEW',
                            },
                        },
                    ],
                    category: [
                        { $: { 'android:name': 'android.intent.category.DEFAULT' } },
                        { $: { 'android:name': 'android.intent.category.BROWSABLE' } },
                    ],
                    data: [
                        {
                            $: {
                                'android:host': 'oauth',
                                'android:scheme': `kakao${props.kakaoAppKey}`,
                            },
                        },
                    ],
                },
            ],
        };
        if (!mainApplication.activity) {
            mainApplication.activity = [];
            mainApplication.activity.push(newPreviewActivity);
            return config;
        }
        const oldActivityIndex = mainApplication.activity.findIndex((activity) => activity.$['android:name'] === ACTIVITY_NAME);
        if (oldActivityIndex < 0) {
            mainApplication.activity.push(newPreviewActivity);
        }
        else {
            mainApplication.activity.splice(oldActivityIndex, 1, newPreviewActivity);
        }
        return config;
    });
};
const modifyAndroidStrings = (config, props) => {
    return (0, config_plugins_1.withStringsXml)(config, (config) => {
        config_plugins_1.AndroidConfig.Strings.setStringItem([{ $: { name: 'kakao_app_key' }, _: props.kakaoAppKey }], config.modResults);
        return config;
    });
};
const modifyProjectBuildGradle = (config, props) => {
    config = (0, config_plugins_1.withGradleProperties)(config, (config) => {
        var _a;
        config_plugins_1.AndroidConfig.BuildProperties.updateAndroidBuildProperty(config.modResults, 'android.kotlinVersion', (_a = props.kotlinVersion) !== null && _a !== void 0 ? _a : '1.5.10');
        return config;
    });
    config = (0, config_plugins_1.withProjectBuildGradle)(config, (config) => {
        var _a;
        if (!config.modResults.contents.includes('org.jetbrains.kotlin:kotlin-gradle-plugin:')) {
            // config.modResults.contents = config.modResults.contents.replace(
            //   `buildToolsVersion = "29.0.3"`,
            //   `buildToolsVersion = "30.0.0"`
            // );
            config.modResults.contents = config.modResults.contents.replace(/dependencies\s?{/, `dependencies {
          classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:${(_a = props.kotlinVersion) !== null && _a !== void 0 ? _a : '1.5.10'}'`);
        }
        return config;
    });
    if (props.overrideKakaoSDKVersion) {
        config = (0, config_plugins_1.withProjectBuildGradle)(config, (config) => {
            var _a, _b, _c;
            const regex = /project.ext {.*\n.*set\('react-native', \[\n.*\]\)\n.*\}/s;
            const match = (_a = regex.exec(config.modResults.contents)) === null || _a === void 0 ? void 0 : _a[0];
            let newMatch;
            if (match) {
                const kakaoSDKRegex = /kakao: \[\n.*\n.*\]/;
                const kakaoSDKMatch = (_b = kakaoSDKRegex.exec(match)) === null || _b === void 0 ? void 0 : _b[0];
                if (kakaoSDKMatch) {
                    newMatch = match.replace(kakaoSDKRegex, `kakao: [
                sdk: "${props.overrideKakaoSDKVersion}",
            ]`);
                    config.modResults.contents = config.modResults.contents.replace(regex, newMatch);
                }
                else {
                    const endRegex = /\],\n *\]\)\n *}/s;
                    const endMatch = (_c = endRegex.exec(match)) === null || _c === void 0 ? void 0 : _c[0];
                    if (endMatch) {
                        newMatch = match.replace(endRegex, `kakao: [
                sdk: "${props.overrideKakaoSDKVersion}",
            ],
        ],
    ])
}`);
                        config.modResults.contents = config.modResults.contents.replace(regex, newMatch);
                    }
                }
                return config;
            }
            config.modResults.contents = config.modResults.contents.concat(`
project.ext {
    set('react-native', [
        versions: [
            kakao: [
                sdk: "${props.overrideKakaoSDKVersion}",
            ],
        ],
    ])
}`);
            return config;
        });
    }
    return config;
};
const withAndroidKakaoLogin = (config, props) => {
    config = modifyAndroidManifest(config, props);
    config = modifyAndroidStrings(config, props);
    config = modifyProjectBuildGradle(config, props);
    return config;
};
exports.withAndroidKakaoLogin = withAndroidKakaoLogin;
