package com.noisepipe.server.payload;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionResponse {
  private Long id;
  private String title;
  private String description;
  private UserSummary createdBy;
  private int bookmarks;
  private Boolean isBookmarked;
  // private List<TagResponse> tags;
}