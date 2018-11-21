package com.noisepipe.server.repository;

import com.noisepipe.server.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}