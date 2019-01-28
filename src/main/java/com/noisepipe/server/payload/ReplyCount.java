package com.noisepipe.server.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReplyCount {
  private Long id;
  private Long replies;
}
