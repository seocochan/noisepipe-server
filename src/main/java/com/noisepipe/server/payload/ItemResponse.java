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
public class ItemResponse {
  private Long id;
  private String title;
  private String description;
  private String sourceUrl;
  private String sourceProvider;
  @Builder.Default
  private List<String> tags = new ArrayList<>();
  private Double position;
  private Long createdBy;
  private Long collectionId;
  private Instant createdAt;
  private Instant updatedAt;
}