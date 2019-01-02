package com.noisepipe.server.payload;

import com.noisepipe.server.utils.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CueRequest {
  @NotNull
  private Integer seconds;

  @Size(max = AppConstants.MAX_CUE_NAME_LENGTH)
  private String name;
}
