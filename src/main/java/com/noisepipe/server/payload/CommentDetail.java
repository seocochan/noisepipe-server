package com.noisepipe.server.payload;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDetail {
  private Long id;
  private String text;
  private CollectionSummary collection;
  private UserSummary createdBy;
  private Instant createdAt;
}
