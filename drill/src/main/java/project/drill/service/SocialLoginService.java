package project.drill.service;

import project.drill.dto.LoginRequestDto;
import project.drill.dto.SocialAuthResponse;
import project.drill.dto.SocialUserResponse;

public interface SocialLoginService {
  public Long doSocialLogin(LoginRequestDto loginRequestDto) throws Exception;
  SocialAuthResponse getAccessToken(String authorizationCode);
  SocialUserResponse getKakaoInfo(String accessToken);
  SocialUserResponse getGoogleInfo(String accessToken);
}
