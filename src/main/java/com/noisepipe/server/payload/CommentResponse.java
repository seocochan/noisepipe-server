package com.noisepipe.server.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponse {
  private Long id;
  private String text;
  private Integer depth;
  private CollectionSummary collection;
  private UserSummary createdBy;
}
