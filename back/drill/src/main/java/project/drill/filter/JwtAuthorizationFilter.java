package project.drill.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.drill.config.auth.MemberDetail;
import project.drill.domain.Member;
import project.drill.repository.MemberRepository;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final Environment env;
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

    // 헤더에서 JWT 를 받아옵니다.
    String header=request.getHeader(JWTKey.REQUIRED_HEADER);
    // 토큰 유무 확인
    if(header==null || !header.startsWith(JWTKey.TOKEN_PREFIX)){
      chain.doFilter(request,response);
      return;
    }

    String token = header.substring(JWTKey.TOKEN_PREFIX.length()).trim();
    String secretKey=env.getProperty("jwt.secret");
    logger.debug("secretKey: "+ secretKey);
    if(secretKey==null) {
      logger.error("JWT key is not exists!!");
      chain.doFilter(request, response);
      return;
    }

    // 토큰의 유효기간 만료 시
    if (jwtTokenProvider.isTokenExpired(token)){
      String errorMessage = "토큰이 만료되었습니다.";
//            ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(),errorMessage);
      response.sendError(HttpStatus.UNAUTHORIZED.value(), errorMessage);
      return;
    }

    // 블랙리스트의 토큰과 대조 시
    if(!jwtTokenProvider.validateToken(token)){
      String errorMessage = "유효하지 않은 토큰입니다.";
      response.sendError(HttpStatus.UNAUTHORIZED.value(), errorMessage);
      return;
    }

    // JWT 토큰에서 memberId 부분만 추출
    String memberId= jwtUtil.extractClaimValue(token, "memberId");

//        String memberId= JWT.require(Algorithm.HMAC512(secretKey)).build()
//                .verify(token)
//                .getClaim("memberId")
//                .asString();

    // token 값을 권한 처리를 위해 Authentication에 주입
    if(memberId!=null){
      Member member=memberRepository.findById(Long.parseLong(memberId)).orElse(null);
//      Member member=memberRepository.findById(Long.parseLong(memberId)).orElse(null);
      MemberDetail memberDetail = new MemberDetail(member);

      UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(memberDetail,null, memberDetail.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
    }

    chain.doFilter(request,response);
  }

  private interface JWTKey{
    public static final String REQUIRED_HEADER="Authorization";
    public static final String TOKEN_PREFIX="Bearer ";
  }

}
