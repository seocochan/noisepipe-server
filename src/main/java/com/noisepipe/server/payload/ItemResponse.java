package com.noisepipe.server.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemResponse {
  private Long id;
  private String title;
  private String description;
  private String sourceUrl;
  private String sourceProvider;
  private String startAt;
  // private List<Tag> tags;
  private Double position;
  private Long createdBy;
  private Long collectionId;
}