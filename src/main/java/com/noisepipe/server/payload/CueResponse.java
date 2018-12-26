package com.noisepipe.server.payload;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CueResponse {
  private Long id;
  private Integer seconds;
  private String name;
  private Instant createdAt;
}
