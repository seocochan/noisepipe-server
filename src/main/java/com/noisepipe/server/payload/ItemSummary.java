package com.noisepipe.server.payload;

import com.noisepipe.server.model.enums.Provider;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemSummary {
  private Long id;
  private String title;
  private String description;
  private String sourceUrl;
  private Provider sourceProvider;
  @Builder.Default
  private List<String> tags = new ArrayList<>();
  private Long collectionId;
  private String collectionTitle;
  private UserSummary createdBy;
  private Instant createdAt;
}
