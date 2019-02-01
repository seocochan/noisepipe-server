package com.noisepipe.server.payload;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSummary {
  private Long id;
  private String username;
}