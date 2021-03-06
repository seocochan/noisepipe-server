package com.noisepipe.server.payload;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponse {
  private Long id;
  private String text;
  private Integer depth;
  private Long replies;
  private UserSummary createdBy;
  private Instant createdAt;
}
