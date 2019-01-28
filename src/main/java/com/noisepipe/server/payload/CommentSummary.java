package com.noisepipe.server.payload;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentSummary {
  private Long id;
  private String text;
  private Long collectionId;
  private String collectionTitle;
  private UserSummary createdBy;
  private Instant createdAt;
}
