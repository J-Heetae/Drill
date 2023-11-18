package project.drill.config.redis;

public interface TokenRevocationService {
  // 액세스 토큰 무효화
  void revokeAccessToken(String userId, String accessToken);

  String getRevokedToken(String userId);
}
