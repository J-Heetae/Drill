package project.drill.service;

import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.drill.domain.Member;
import project.drill.domain.Role;
import project.drill.dto.GoogleServerResponse;
import project.drill.dto.KaKaoServerResponse;
import project.drill.dto.LoginRequestDto;
import project.drill.dto.SocialAuthResponse;
import project.drill.dto.SocialUserResponse;
import project.drill.google.GoogleAuthApi;
import project.drill.google.GoogleUserApi;
import project.drill.util.GsonLocalDateTimeAdapter;
import project.drill.kakao.KakaoAuthApi;
import project.drill.kakao.KakaoUserApi;
import project.drill.repository.MemberRepository;
import com.google.gson.Gson;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService {
  private final KakaoAuthApi kakaoAuthApi;
  private final KakaoUserApi kakaoUserApi;
  private final GoogleUserApi googleUserApi;

  @Value("${social.client.kakao.rest-api-key}")
  private String kakaoAppKey;
  @Value("${social.client.kakao.redirect-uri}")
  private String kakaoRedirectUri;
  @Value("${social.client.kakao.grant_type}")
  private String kakaoGrantType;

  private final MemberRepository memberRepository;

  @Transactional
  public Long doSocialLogin(LoginRequestDto loginRequestDto) throws Exception {
    System.out.println("SNS 타입 : " + loginRequestDto.getType());
    SocialUserResponse socialUserResponse = null;
    if(loginRequestDto.getType().equals("kakao")) {
      // 카카오 로그인
      socialUserResponse = getKakaoInfo(loginRequestDto.getSocialToken());
    } else {
      // 구글 로그인
      socialUserResponse = getGoogleInfo(loginRequestDto.getSocialToken());
    }

    System.out.println("아이디: " + socialUserResponse.getId());
    System.out.println(memberRepository.findByMemberEmail(socialUserResponse.getId()));
    if (!memberRepository.findByMemberEmail(socialUserResponse.getId()).isPresent()) {
      Member member = new Member(null, socialUserResponse.getId(),
          null, null, null, Role.ROLE_BEFORE, new Long(0), new Long(100), null );
      System.out.println("save member");
      memberRepository.save(member);
    }

    Member member = memberRepository.findByMemberEmail(socialUserResponse.getId())
        .orElseThrow(() -> new NotFoundException());

    return member.getMemberId();
  }

  @Override
  public SocialAuthResponse getAccessToken(String authorizationCode) {
    ResponseEntity<?> response = kakaoAuthApi.getAccessToken(
        kakaoAppKey,
        kakaoGrantType,
        kakaoRedirectUri,
        authorizationCode
    );

    log.info("kakao auth response {}", response.toString());

    return new Gson()
        .fromJson(
            String.valueOf(response.getBody())
            , SocialAuthResponse.class
        );
  }

  @Override
  public SocialUserResponse getKakaoInfo(String accessToken) {
    Map<String ,String> headerMap = new HashMap<>();
    headerMap.put("authorization", "Bearer " + accessToken);

    ResponseEntity<?> response = kakaoUserApi.getUserInfo(headerMap);

    log.info("kakao user response");
    log.info(response.toString());

    String jsonString = response.getBody().toString();

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    KaKaoServerResponse kaKaoServerResponse = gson.fromJson(jsonString, KaKaoServerResponse.class);
    KaKaoServerResponse.KakaoLoginData kakaoLoginData = Optional.ofNullable(kaKaoServerResponse.getKakao_account())
        .orElse(KaKaoServerResponse.KakaoLoginData.builder().build());

    String name = Optional.ofNullable(kakaoLoginData.getProfile())
        .orElse(KaKaoServerResponse.KakaoLoginData.KakaoProfile.builder().build())
        .getNickname();

    return SocialUserResponse.builder()
        .id(kaKaoServerResponse.getId())
        .gender(kakaoLoginData.getGender())
        .name(name)
        .email(kakaoLoginData.getEmail())
        .build();
  }

  @Override
  public SocialUserResponse getGoogleInfo(String accessToken) {
    ResponseEntity<?> response = googleUserApi.getUserInfo(accessToken);

    log.info("google user response");
    log.info(response.toString());

    String jsonString = response.getBody().toString();

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    GoogleServerResponse googleServerResponse = gson.fromJson(jsonString, GoogleServerResponse.class);

    return SocialUserResponse.builder()
        .id(googleServerResponse.getId())
        .email(googleServerResponse.getEmail())
        .build();
  }
}
