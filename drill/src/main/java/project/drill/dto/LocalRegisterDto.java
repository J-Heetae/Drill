package project.drill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalRegisterDto {
  private String email;
  private String password;
}
