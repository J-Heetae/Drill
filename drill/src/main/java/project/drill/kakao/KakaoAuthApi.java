package project.drill.kakao;

import org.springframework.context.annotation.ComponentScan;
import project.drill.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakaoAuth", url="https://kauth.kakao.com", configuration = {FeignConfiguration.class})
public interface KakaoAuthApi {
    @PostMapping("/oauth/token")
    ResponseEntity<String> getAccessToken(
            @RequestParam("client_id") String clientId,
            //@RequestParam(value = "client_secret", required = false, defaultValue = "secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String authorizationCode
    );
}
