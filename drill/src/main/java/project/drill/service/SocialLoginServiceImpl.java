package project.drill.service;

import com.google.gson.GsonBuilder;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.drill.domain.Member;
import project.drill.domain.Role;
import project.drill.dto.KaKaoLoginResponse;
import project.drill.dto.KakaoLoginDto;
import project.drill.dto.SocialAuthResponse;
import project.drill.dto.SocialLoginDto;
import project.drill.dto.SocialUserResponse;
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

  @Value("${social.client.kakao.rest-api-key}")
  private String kakaoAppKey;
  @Value("${social.client.kakao.redirect-uri}")
  private String kakaoRedirectUri;
  @Value("${social.client.kakao.grant_type}")
  private String kakaoGrantType;

  private final MemberRepository memberRepository;

  public Long doSocialLogin(KakaoLoginDto kakaoLoginDto) throws Exception {
    SocialUserResponse socialUserResponse = getUserInfo(kakaoLoginDto.getAccessToken());
    System.out.println("이메일: " + socialUserResponse.getEmail());
    System.out.println(memberRepository.findByMemberEmail(socialUserResponse.getEmail()));
    if (!memberRepository.findByMemberEmail(socialUserResponse.getEmail()).isPresent()) {
      Member member = new Member(null, socialUserResponse.getEmail(),
          null, null, Role.ROLE_BEFORE, new Long(0), new Long(100), null );
      System.out.println("save member");
      memberRepository.save(member);
    }

    Member member = memberRepository.findByMemberEmail(socialUserResponse.getEmail())
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
  public SocialUserResponse getUserInfo(String accessToken) {
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

    KaKaoLoginResponse kaKaoLoginResponse = gson.fromJson(jsonString, KaKaoLoginResponse.class);
    KaKaoLoginResponse.KakaoLoginData kakaoLoginData = Optional.ofNullable(kaKaoLoginResponse.getKakao_account())
        .orElse(KaKaoLoginResponse.KakaoLoginData.builder().build());

    String name = Optional.ofNullable(kakaoLoginData.getProfile())
        .orElse(KaKaoLoginResponse.KakaoLoginData.KakaoProfile.builder().build())
        .getNickname();

    return SocialUserResponse.builder()
        .id(kaKaoLoginResponse.getId())
        .gender(kakaoLoginData.getGender())
        .name(name)
        .email(kakaoLoginData.getEmail())
        .build();
  }
}
