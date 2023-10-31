package project.drill.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import project.drill.config.redis.TokenRevocationService;

@Component
@Slf4j
public class JwtTokenProvider {

    private final String SECRET_KEY;

    private final JwtUtil jwtUtil;

    private final TokenRevocationService tokenRevocationService;

    @Autowired
    public JwtTokenProvider(Environment env, JwtUtil jwtUtil, TokenRevocationService tokenRevocationService) {
        this.SECRET_KEY = env.getProperty("jwt.secret");
        this.jwtUtil = jwtUtil;
        this.tokenRevocationService = tokenRevocationService;
    }

    public String generateToken(String username, int expiration_time, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(jwtUtil.generateExpirationDate(expiration_time))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // 토큰에서 회원 정보 추출 (이메일 정보)
    public String getIdFromToken(String token) {
        String exractedToken = token.substring(JwtProperties.TOKEN_PREFIX.length()).trim();
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(exractedToken)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        String memberId = jwtUtil.extractClaimValue(token, "memberId");
        String revokedToken = tokenRevocationService.getRevokedToken(memberId);
        System.out.println("revokedToken" + revokedToken);
        return !token.equals(revokedToken);
    }

    public boolean isTokenExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return true;
        }
    }

    // 엑세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    }

    // 리프레시 토큰 헤더 설정
    // 사용자로부터 헤더 값으로 리프레시 토큰을 받는 것을 테스트하는 용도로, 실제 구현에서는 쿠키 값으로 전달하므로 빼야 함
    public void addHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.addHeader(JwtProperties.REFRESH_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + refreshToken);
    }
}
