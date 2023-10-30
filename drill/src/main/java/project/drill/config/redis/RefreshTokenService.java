package project.drill.config.redis;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.drill.filter.JwtUtil;

@Service
public class RefreshTokenService {

  @Autowired
  private JwtUtil jwtUtil;

  private final RedisTemplate<String, Object> redisTemplate;

  public RefreshTokenService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void saveRefreshToken(String userId, String refreshToken, long expirationSeconds) {
    String key = RedisKey.REFRESH_TOKEN_KEY_PREFIX + userId;
    redisTemplate.opsForValue().set(key, refreshToken);
    redisTemplate.expire(key, expirationSeconds, TimeUnit.SECONDS);
  }

  public String getRefreshToken(String userId) {
    String key = RedisKey.REFRESH_TOKEN_KEY_PREFIX + userId;
    return (String) redisTemplate.opsForValue().get(key);
  }

  public void deleteRefreshToken(String userId) {
    String key = RedisKey.REFRESH_TOKEN_KEY_PREFIX + userId;
    redisTemplate.delete(key);
  }
}
