package project.drill.filter;

public interface JwtProperties {
  //    864000000 -> 10일 (1/1000초)
//    int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분
  int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 10 * 1000 ; // 10일
  int REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000 * 14; // 14일
  String TOKEN_PREFIX = "Bearer ";
  String REFRESH_TOKEN_HEADER = "RefreshToken";

  String HEADER_STRING = "Authorization";
}
