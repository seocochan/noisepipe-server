package com.noisepipe.server.repository;

import com.noisepipe.server.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
  Optional<Bookmark> findByUserIdAndCollectionId(Long userId, Long collectionId);

  @Query(value = "SELECT collection_id FROM bookmarks WHERE user_id = ?1 ORDER BY created_at DESC",
          nativeQuery = true)
  List<Long> findAllCollectionIds(Long userId);

  Boolean existsByUserIdAndCollectionId(Long userId, Long collectionId);

  /**
   * Reference:
   * https://stackoverflow.com/a/38104386/10114911
   */
  @Query(value = "SELECT rownum " +
          "FROM ( " +
          "  SELECT collection_id, @rownum \\:= @rownum + 1 AS rownum " +
          "  FROM bookmarks b, (SELECT @rownum \\:= 0) r " +
          "  WHERE user_id = :userId " +
          "  ORDER BY created_at DESC" +
          ") rows " +
          "WHERE rows.collection_id = :offsetId",
          nativeQuery = true)
  Optional<Double> getRownumById(@Param("userId") Long userId, @Param("offsetId") Long offsetId);
}