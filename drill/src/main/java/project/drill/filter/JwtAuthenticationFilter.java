package project.drill.filter;

import static project.drill.filter.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static project.drill.filter.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.drill.config.auth.MemberDetail;
import project.drill.config.redis.RefreshTokenService;
import project.drill.dto.MemberLoginRequestDto;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;

  private final RefreshTokenService refreshTokenService;

  private final JwtTokenProvider jwtTokenProvider;

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtTokenProvider jwtTokenProvider, JwtUtil jwtUtil, String processUrl) {
    this.authenticationManager = authenticationManager;
    this.refreshTokenService = refreshTokenService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.jwtUtil = jwtUtil;
    setFilterProcessesUrl(processUrl);
  }

  // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
  // 인증 요청시에 실행되는 함수 => /login
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    // request에 있는 email과 password를 파싱해서 자바 Object로 받기
    ObjectMapper om = new ObjectMapper();
    MemberLoginRequestDto loginRequestDto = null;
    try {
      loginRequestDto = om.readValue(request.getInputStream(), MemberLoginRequestDto.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("JwtAuthenticationFilter : " + loginRequestDto);

    // 이메일패스워드 토큰 생성
    // 패스워드는 안쓰므로 기본 1234로 박음
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),
        "1234");
    System.out.println("JwtAuthenticationFilter : 토큰생성완료");

    // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
    // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
    // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
    // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
    // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

    // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
    // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
    // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
    Authentication authentication =
        authenticationManager.authenticate(token);
    MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();
    if (memberDetail.getMember() != null)
      System.out.println("Authentication : " + memberDetail.getMember().getMemberEmail());
    return authentication;
  }

  // JWT Token 생성해서 response에 담아주기
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    MemberDetail memberDetail = (MemberDetail) authResult.getPrincipal();

    Map<String, Object> customClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(memberDetail.getMember().getMemberId()));

    String accessToken = jwtTokenProvider.generateToken(memberDetail.getUsername(), ACCESS_TOKEN_EXPIRATION_TIME, customClaims);
    String refreshToken = jwtTokenProvider.generateToken(memberDetail.getUsername(), REFRESH_TOKEN_EXPIRATION_TIME, customClaims);

    jwtTokenProvider.setHeaderAccessToken(response, accessToken);

    // 사용자로부터 헤더 값으로 리프레시 토큰을 받는 것을 테스트하는 용도로, 실제 구현에서는 쿠키 값으로 전달하므로 빼야 함
    jwtTokenProvider.addHeaderRefreshToken(response, refreshToken);

//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    refreshTokenService.saveRefreshToken(String.valueOf(memberDetail.getMember().getMemberId()), refreshToken, REFRESH_TOKEN_EXPIRATION_TIME);

    // RefreshToken을 쿠키에 담아 전송할 경우
//        jwtTokenProvider.addRefreshTokenToCookie(response, refreshToken);
  }
}
