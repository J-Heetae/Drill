package project.drill.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import project.drill.config.redis.RefreshTokenService;
import project.drill.config.redis.TokenRevocationService;
import project.drill.filter.JwtUtil;

//@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final TokenRevocationService tokenRevocationService;

    public CustomLogoutSuccessHandler(RefreshTokenService refreshTokenService, JwtUtil jwtUtil, TokenRevocationService tokenRevocationService) {
        super();
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.tokenRevocationService = tokenRevocationService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String accessToken = jwtUtil.resolveToken(request);
        String memberId = jwtUtil.extractClaimValue(accessToken, "memberId");
        refreshTokenService.deleteRefreshToken(memberId);

        // accessToken BlackList 설정
        tokenRevocationService.revokeAccessToken(memberId, accessToken);
    }
}
