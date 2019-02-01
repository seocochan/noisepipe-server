package com.noisepipe.server.repository;

import com.noisepipe.server.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
  Page<Collection> findByUserUsername(String username, Pageable pageable);

  @Query("SELECT c FROM Collection c JOIN c.tags t WHERE t.name = :tagName")
  Page<Collection> findByTagName(@Param("tagName") String tagName, Pageable pageable);

  @Query(value = "SELECT * FROM collections WHERE id IN ?1 ORDER BY FIELD(id, ?1)",
          countQuery = "SELECT COUNT(*) FROM collections WHERE id IN ?1",
          nativeQuery = true)
  Page<Collection> findByIdInOrderByField(List<Long> id, Pageable pageable);

  Page<Collection> findByBookmarksUserUsername(String username, Pageable pageable);

  /**
   * Reference:
   * https://stackoverflow.com/a/38104386/10114911
   */
  @Query(value = "SELECT rownum " +
          "FROM ( " +
          "  SELECT id, @rownum \\:= @rownum + 1 AS rownum " +
          "  FROM collections c, (SELECT @rownum \\:= 0) r " +
          "  WHERE user_id = :userId " +
          "  ORDER BY created_at DESC" +
          ") rows " +
          "WHERE rows.id = :offsetId",
          nativeQuery = true)
  Optional<Double> getRownumById(@Param("userId") Long userId, @Param("offsetId") Long offsetId);

  Page<Collection> findByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(
          String title, String tagsName, Pageable pageable);
}