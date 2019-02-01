package com.noisepipe.server.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {
  private Long id;
  private String username;
  private Instant joinedAt;
}