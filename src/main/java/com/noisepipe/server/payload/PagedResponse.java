package com.noisepipe.server.payload;

import lombok.*;
import org.springframework.data.domain.Page;

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

  public static <T> PagedResponse<T> of(List<T> content, Page<?> page) {
    return new PagedResponse<>(content,
            page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
  }
}