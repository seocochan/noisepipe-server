package com.noisepipe.server.payload;

import com.noisepipe.server.model.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemPostRequest {

  @NotBlank
  @Size(max = 100)
  private String title;

  @NotBlank
  private String sourceUrl;

  @NotNull
  private Provider sourceProvider;

  @NotNull
  private Double position;
}