package com.noisepipe.server.payload;

import com.noisepipe.server.utils.AppConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {

  @NotBlank
  @Size(min = AppConstants.MIN_USERNAME_LENGTH, max = AppConstants.MAX_USERNAME_LENGTH)
  @Pattern(regexp = "^\\w+$")
  private String username;

  @NotBlank
  @Size(min = AppConstants.MIN_PASSWORD_LENGTH, max = AppConstants.MAX_PASSWORD_LENGTH)
  private String password;
}
