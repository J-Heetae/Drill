package project.drill.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SocialLoginDto {
  @NotNull
  private String snsType;
  @NotNull
  private String code;
}
