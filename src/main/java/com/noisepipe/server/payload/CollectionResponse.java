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
public class CollectionResponse {
  private Long id;
  private String title;
  private String description;
  @Builder.Default
  private List<String> tags = new ArrayList<>();
  private int bookmarks;
  private Boolean isBookmarked;
  private UserSummary createdBy;
  private Instant createdAt;
}