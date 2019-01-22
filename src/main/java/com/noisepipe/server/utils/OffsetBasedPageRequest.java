package com.noisepipe.server.utils;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Reference:
 * https://stackoverflow.com/a/36365522/10114911
 */

@EqualsAndHashCode
@ToString
public class OffsetBasedPageRequest implements Pageable, Serializable {

  private static final long serialVersionUID = -25822477129613575L;

  private long offset;
  private int size;
  private final Sort sort;

  public OffsetBasedPageRequest(long offset, int size, Sort sort) {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset index must not be less than zero!");
    }
    if (size < 1) {
      throw new IllegalArgumentException("Size must not be less than one!");
    }
    this.offset = offset;
    this.size = size;
    this.sort = sort;
  }

  public OffsetBasedPageRequest(long offset, int size, Sort.Direction direction, String... properties) {
    this(offset, size, new Sort(direction, properties));
  }

  public OffsetBasedPageRequest(long offset, int size) {
    this(offset, size, new Sort(Sort.Direction.ASC, "id"));
  }

  @Override
  public int getPageNumber() {
    return (int) (offset / size);
  }

  @Override
  public int getPageSize() {
    return size;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return new OffsetBasedPageRequest(getOffset() + getPageSize(), getPageSize(), getSort());
  }

  public OffsetBasedPageRequest previous() {
    return hasPrevious() ? new OffsetBasedPageRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
  }

  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @Override
  public Pageable first() {
    return new OffsetBasedPageRequest(0, getPageSize(), getSort());
  }

  @Override
  public boolean hasPrevious() {
    return offset > size;
  }
}
