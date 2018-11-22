package com.noisepipe.server.repository;

import com.noisepipe.server.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
  Page<Collection> findByUserId(Long userId, Pageable pageable);

  @Query("SELECT c FROM Collection c JOIN c.tags t WHERE t.name = :tagName")
  Page<Collection> findByTagName(@Param("tagName") String tagName, Pageable pageable);

  @Query(value = "SELECT * FROM collections WHERE id IN ?1 ORDER BY FIELD(id, ?1)",
          countQuery = "SELECT COUNT(*) FROM collections WHERE id IN ?1",
          nativeQuery = true)
  Page<Collection> findByIdInOrderByField(List<Long> id, Pageable pageable);
}