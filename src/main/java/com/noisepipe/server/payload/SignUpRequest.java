package com.noisepipe.server.payload;

import com.noisepipe.server.utils.AppConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {

  @NotBlank
  @Size(min = AppConstants.MIN_NAME_LENGTH, max = AppConstants.MAX_NAME_LENGTH)
  private String name;

  @NotBlank
  @Size(min = AppConstants.MIN_USERNAME_LENGTH, max = AppConstants.MAX_USERNAME_LENGTH)
  private String username;

  @NotBlank
  @Size(max = AppConstants.MAX_EMAIL_LENGTH)
  private String email;

  @NotBlank
  @Size(min = AppConstants.MIN_PASSWORD_LENGTH, max = AppConstants.MAX_PASSWORD_LENGTH)
  private String password;
}
