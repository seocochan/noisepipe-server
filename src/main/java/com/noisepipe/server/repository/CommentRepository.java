package com.noisepipe.server.repository;

import com.noisepipe.server.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByUserUsername(String username, Pageable pageable);

  Page<Comment> findByCollectionId(Long collectionId, Pageable pageable);
}