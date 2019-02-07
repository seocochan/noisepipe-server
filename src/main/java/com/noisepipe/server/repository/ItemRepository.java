package com.noisepipe.server.repository;

import com.noisepipe.server.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
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

  @Procedure(name = "resetItemsPosition")
  void resetPosition(@Param("collection_id") Long collectionId, @Param("user_id") Long userId, @Param("unit") Double unit);

  Page<Item> findDistinctByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(
          String title, String tagsName, Pageable pageable);

  Long countByCollectionId(Long collectionId);

  @Query("SELECT i.collection.id FROM Item i GROUP BY i.collection ORDER BY MAX(i.createdAt) DESC")
  List<Long> findRecentlyUpdatedCollectionIds(Pageable pageable);
}