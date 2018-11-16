package com.noisepipe.server.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionResponse {
  private Long id;
  private String title;
  private String description;
  private UserSummary createdBy;
  // private List<ItemResponse> items;
  // private List<CommentResponse> comments;
  // private Integer bookmarks;
  // private List<TagResponse> tags;
}
