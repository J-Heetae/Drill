package project.drill.config;


import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.AntPathRequestMatcherProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import project.drill.config.redis.RefreshTokenService;
import project.drill.config.redis.TokenRevocationService;
import project.drill.filter.JwtAuthenticationFilter;
import project.drill.filter.JwtAuthorizationFilter;
import project.drill.filter.JwtTokenProvider;
import project.drill.filter.JwtUtil;
import project.drill.handler.AccessDenyHandler;
import project.drill.handler.CustomAuthenticationEntryPoint;
import project.drill.handler.CustomLogoutSuccessHandler;
import project.drill.repository.MemberRepository;
import project.drill.domain.Role;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final Environment env;

  private final RefreshTokenService refreshTokenService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private CorsConfig corsConfig;

  @Autowired
  private TokenRevocationService tokenRevocationService;
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  //권한 확인을 하지 않는 uri
  private static final String[] PERMIT_ALL_PATTERNS = new String[] {
      "/v2/api-docs",
      "/v2/api-docs/**",
      "/configuration/**",
      "/swagger*/**",
      "/webjars/**",
      "/swagger-ui/**",
      "/docs",
      "/api/login",
      "/api/user/**",
  };

  // 경로 접근 설정
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http
        .addFilter(corsConfig.corsFilter())
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin().disable()
        .httpBasic().disable()
        .addFilter(new JwtAuthenticationFilter(authenticationManager, refreshTokenService, jwtTokenProvider, jwtUtil, "/api/login"))
        .addFilterBefore(new JwtAuthorizationFilter(env, memberRepository, jwtTokenProvider, jwtUtil), BasicAuthenticationFilter.class)
        .authorizeHttpRequests()
        .requestMatchers(
            new AntPathRequestMatcher("/v2/api-docs"),
            new AntPathRequestMatcher("/v2/api-docs/**"),
            new AntPathRequestMatcher("/configuration/**"),
            new AntPathRequestMatcher("/swagger*/**"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/docs"),
            new AntPathRequestMatcher("/api/member/login"),
            new AntPathRequestMatcher("api/member/refresh")
        )
        .permitAll()
        .requestMatchers(new AntPathRequestMatcher("/**", "OPTIONS")).permitAll()
//        .requestMatchers(new AntPathRequestMatcher("/api/member", "GET")).hasAnyAuthority("ROLE_ADMIN", "ROLE_BEFORE", "ROLE_BOSS", "ROLE_DONE")
//        .requestMatchers(new AntPathRequestMatcher("/api/member/id-check", "POST")).permitAll()
//        .requestMatchers(new AntPathRequestMatcher("/api/member", "POST")).permitAll()
//        .requestMatchers(new AntPathRequestMatcher("/api/member/**", "GET")).hasAnyAuthority("ROLE_ADMIN", "ROLE_BEFORE", "ROLE_BOSS", "ROLE_DONE")
//        .requestMatchers(new AntPathRequestMatcher("/api/member/**", "PATCH")).hasAnyAuthority("ROLE_ADMIN", "ROLE_BEFORE", "ROLE_BOSS", "ROLE_DONE")
//        .requestMatchers(new AntPathRequestMatcher("/api/member/**", "PUT")).hasAnyAuthority("ROLE_ADMIN", "ROLE_BEFORE", "ROLE_BOSS", "ROLE_DONE")
//        .requestMatchers(new AntPathRequestMatcher("/api/member/**", "DELETE")).hasAnyAuthority("ROLE_ADMIN", "ROLE_BEFORE", "ROLE_BOSS", "ROLE_DONE")
//        .requestMatchers(new AntPathRequestMatcher("/api/member", "DELETE")).hasAnyAuthority("ROLE_ADMIN", "ROLE_BEFORE", "ROLE_BOSS", "ROLE_DONE")
        .anyRequest().permitAll();
        // .anyRequest().authenticated()
//        .and()
//        .logout()
//        .logoutRequestMatcher(new AntPathRequestMatcher("/api/member/logout"))
//        .invalidateHttpSession(true)
//        .logoutSuccessHandler(new CustomLogoutSuccessHandler(refreshTokenService, jwtUtil, tokenRevocationService));

    http.exceptionHandling().accessDeniedHandler(new AccessDenyHandler());
    http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
