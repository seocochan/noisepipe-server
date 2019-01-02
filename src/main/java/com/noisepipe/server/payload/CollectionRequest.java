package com.noisepipe.server.payload;

import com.noisepipe.server.utils.AppConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CollectionRequest {
  @NotBlank
  @Size(max = AppConstants.MAX_COLLECTION_TITLE_LENGTH)
  private String title;

  @Size(max = AppConstants.MAX_COLLECTION_DESCRIPTION_LENGTH)
  private String description;

  @Size(max = AppConstants.MAX_COLLECTION_TAGS_SIZE)
  private List<@NotBlank @Size(max = AppConstants.MAX_TAG_NAME_LENGTH) String> tags = new ArrayList<>();
}