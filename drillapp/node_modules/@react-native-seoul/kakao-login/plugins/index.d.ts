import { ConfigPlugin } from '@expo/config-plugins';
export interface KakaoLoginPluginProps {
    kakaoAppKey: string;
    overrideKakaoSDKVersion?: string;
    kotlinVersion?: string;
}
declare const _default: ConfigPlugin<KakaoLoginPluginProps>;
export default _default;
