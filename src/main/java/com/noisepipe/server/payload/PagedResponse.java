package com.noisepipe.server.payload;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagedResponse<T> {

  private List<T> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
  @Getter(AccessLevel.NONE)
  private boolean last;

  public boolean isLast() {
    return last;
  }
}