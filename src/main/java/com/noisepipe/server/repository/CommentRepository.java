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
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByUserUsername(String username, Pageable pageable);

  List<Comment> findByCollectionIdAndDepthAndReplyTo(Long collectionId, Integer depth, Long replyTo, Sort sort);

  @Query("SELECT new com.noisepipe.server.payload.ReplyCount(c.replyTo, COUNT(c)) " +
          "FROM Comment c " +
          "WHERE c.collection.id = :collectionId AND c.depth = :depth " +
          "GROUP BY c.replyTo")
  List<ReplyCount> findReplyCount(@Param("collectionId") Long collectionId, @Param("depth") Integer depth);

  void deleteAllByReplyTo(Long replyTo);

  /**
   * Reference:
   * https://stackoverflow.com/a/38104386/10114911
   */
  @Query(value = "SELECT rownum " +
          "FROM ( " +
          "  SELECT id, @rownum \\:= @rownum + 1 AS rownum " +
          "  FROM comments c, (SELECT @rownum \\:= 0) r " +
          "  WHERE user_id = :userId " +
          "  ORDER BY created_at DESC" +
          ") rows " +
          "WHERE rows.id = :offsetId",
          nativeQuery = true)
  Optional<Double> getRownumById(@Param("userId") Long userId, @Param("offsetId") Long offsetId);
}