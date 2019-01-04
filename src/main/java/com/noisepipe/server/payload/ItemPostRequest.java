package com.noisepipe.server.payload;

import com.noisepipe.server.model.enums.Provider;
import com.noisepipe.server.utils.AppConstants;
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
  @Size(max = AppConstants.MAX_ITEM_TITLE_LENGTH)
  private String title;

  @NotBlank
  private String sourceUrl;

  @NotNull
  private Provider sourceProvider;

  @NotNull
  private Double position;
}