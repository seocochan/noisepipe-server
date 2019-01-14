package com.noisepipe.server.payload;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionSummary {
  private Long id;
  private String title;
  private UserSummary createdBy;
  private Instant createdAt;
}