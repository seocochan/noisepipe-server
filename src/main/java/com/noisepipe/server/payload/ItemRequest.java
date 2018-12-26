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
public class ItemRequest {

  @NotBlank
  @Size(max = 40)
  private String title;

  @Size(max = 255)
  private String description;

  @NotBlank
  private String sourceUrl;

  // @NotBlank
  // private String sourceProvider;

  @Size(max = 5)
  private List<@NotBlank @Size(max = 40) String> tags = new ArrayList<>();

  @NotNull
  private Double position;
}