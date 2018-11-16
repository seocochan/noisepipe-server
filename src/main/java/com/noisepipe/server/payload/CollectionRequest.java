package com.noisepipe.server.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CollectionRequest {
  @NotBlank
  @Size(max = 40)
  private String title;

  @Size(max = 255)
  private String description;
}
