package project.drill.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import project.drill.config.FeignConfiguration;

@FeignClient(value = "googleAuth", url="https://oauth2.googleapis.com", configuration = {
    FeignConfiguration.class})
public interface GoogleAuthApi {
  @PostMapping("/token")
  ResponseEntity<String> getAccessToken(
      @RequestParam("code") String code,
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("redirect_uri") String redirectUri,
      @RequestParam("grant_type") String grantType
  );
}
