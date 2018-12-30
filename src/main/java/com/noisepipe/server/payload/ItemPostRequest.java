package com.noisepipe.server.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

  @NotBlank
  private String sourceProvider;

  @NotNull
  private Double position;
}