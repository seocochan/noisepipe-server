package com.noisepipe.server.repository;

import com.noisepipe.server.model.Comment;
import com.noisepipe.server.payload.ReplyCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByUserId(Long userId, Pageable pageable);

  List<Comment> findByCollectionIdAndDepthAndReplyTo(Long collectionId, Integer depth, Long replyTo, Sort sort);

  @Query("SELECT new com.noisepipe.server.payload.ReplyCount(c.replyTo, COUNT(c)) " +
          "FROM Comment c " +
          "WHERE c.collection.id = :collectionId AND c.depth = :depth " +
          "GROUP BY c.replyTo")
  List<ReplyCount> findReplyCount(@Param("collectionId") Long collectionId, @Param("depth") Integer depth);

  void deleteAllByReplyTo(Long replyTo);
}