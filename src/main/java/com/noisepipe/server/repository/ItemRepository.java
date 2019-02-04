package com.noisepipe.server.repository;

import com.noisepipe.server.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  List<Item> findByCollectionId(Long collectionId, Sort sort);

  @Query("SELECT i FROM Item i JOIN i.tags t WHERE t.name = :tagName")
  Page<Item> findByTagName(@Param("tagName") String tagName, Pageable pageable);

  @Modifying
  @Query("UPDATE Item i SET i.position = :position WHERE i.id = :id AND i.createdBy = :userId")
  int updatePosition(@Param("id") Long id,
                     @Param("userId") Long userId,
                     @Param("position") Double position);

  /**
   * Reference:
   * https://stackoverflow.com/a/10485817/10114911
   */
  @Modifying
  @Query(value = "UPDATE items target " +
          "JOIN ( " +
          "  SELECT id, (@rownum \\:= @rownum + :unit) as rownum " +
          "  FROM items i " +
          "  CROSS JOIN (SELECT @rownum \\:= 0) r " +
          "  WHERE collection_id = :collectionId AND created_by = :userId " +
          "  ORDER BY i.position ASC  " +
          ") SOURCE ON target.id = source.id " +
          "SET position = rownum " +
          "WHERE collection_id = :collectionId AND created_by = :userId",
          nativeQuery = true)
  int resetPosition(@Param("collectionId") Long collectionId, @Param("userId") Long userId, @Param("unit") double unit);

  Page<Item> findDistinctByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(
          String title, String tagsName, Pageable pageable);

  Long countByCollectionId(Long collectionId);
}