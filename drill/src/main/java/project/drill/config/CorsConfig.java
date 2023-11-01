package project.drill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import project.drill.filter.JwtProperties;

@Configuration
public class CorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true); // 인증 헤더 사용 여부 설정
//        config.addAllowedOrigin("*"); // 허용할 오리진(도메인) 설정
    config.addAllowedOriginPattern("*"); // 허용할 오리진(도메인) 설정
    config.addAllowedHeader("*"); // 허용할 HTTP 헤더 설정
    config.addAllowedMethod("*"); // 허용할 HTTP 메소드 설정
    config.addExposedHeader(JwtProperties.HEADER_STRING);
    config.addExposedHeader(JwtProperties.REFRESH_TOKEN_HEADER);


    source.registerCorsConfiguration("/api/**", config);
    return new CorsFilter(source);
  }

}