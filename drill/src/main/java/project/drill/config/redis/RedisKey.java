package project.drill.config.redis;

public interface RedisKey {
  String REFRESH_TOKEN_KEY_PREFIX = "Bearer ";
  String REVOKED_TOKEN_KEY_PREFIX = "Invalid ";

  String EMAIL_AUTH_SUCCESS_TOKEN_PREFIX="Email-Auth-Success-Token: ";
  String EMAIL_AUTH_TOKEN_PREFIX="Email-Auth-Token: "; // 이메일 인증 토큰
  int REVOKED_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000 ; // 60분;

  int EMAIL_AUTH_TOKEN_EXPIRATION_TIME=5*60*1000;
  int EMAIL_AUTH_SUCCESS_TOKEN_EXPIRATION_TIME=5*60*1000;
}
