package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalLoginDto {
  private String email;
  private String password;
}
