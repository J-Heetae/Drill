package project.drill.service;

import project.drill.dto.KakaoLoginDto;
import project.drill.dto.SocialAuthResponse;
import project.drill.dto.SocialUserResponse;

public interface SocialLoginService {
  public Long doSocialLogin(KakaoLoginDto kakaoLoginDto) throws Exception;
  SocialAuthResponse getAccessToken(String authorizationCode);
  SocialUserResponse getUserInfo(String accessToken);
}
