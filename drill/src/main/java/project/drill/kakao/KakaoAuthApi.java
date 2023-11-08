package project.drill.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.drill.config.FeignConfiguration;

@FeignClient(value = "kakaoAuth", url = "https://kauth.kakao.com", configuration = {FeignConfiguration.class})
public interface KakaoAuthApi {
	@PostMapping("/oauth/token")
	ResponseEntity<String> getAccessToken(
		@RequestParam("client_id") String clientId,
		@RequestParam("grant_type") String grantType,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("code") String authorizationCode
	);
}