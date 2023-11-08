package project.drill.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
  private String type;
  private String socialToken;
}
