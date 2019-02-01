package com.noisepipe.server.repository;

import com.noisepipe.server.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  Page<Item> findByCollectionId(Long collectionId, Pageable pageable);

  @Query("SELECT i FROM Item i JOIN i.tags t WHERE t.name = :tagName")
  Page<Item> findByTagName(@Param("tagName") String tagName, Pageable pageable);

  @Modifying
  @Query("UPDATE Item i SET i.position = :position WHERE i.id = :id AND i.createdBy = :userId")
  int updatePosition(@Param("id") Long id,
                     @Param("userId") Long userId,
                     @Param("position") Double position);

  Page<Item> findByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(
          String title, String tagsName, Pageable pageable);
}