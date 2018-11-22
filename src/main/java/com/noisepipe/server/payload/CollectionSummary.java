package com.noisepipe.server.payload;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionSummary {
  private Long id;
  private String title;
  private String description;
  private int items;
  private UserSummary createdBy;
}