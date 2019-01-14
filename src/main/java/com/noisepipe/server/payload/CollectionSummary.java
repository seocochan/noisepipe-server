package com.noisepipe.server.payload;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionSummary {
  private Long id;
  private String title;
  private int items;
  @Builder.Default
  private List<String> tags = new ArrayList<>();
  private UserSummary createdBy;
  private Instant createdAt;
}