package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.Comment;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.*;
import com.noisepipe.server.repository.CollectionRepository;
import com.noisepipe.server.repository.CommentRepository;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CollectionRepository collectionRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public CommentResponse createComment(User user, Long collectionId, CommentRequest commentRequest) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));

    int depth = 0;
    Long replyTo = commentRequest.getReplyTo();
    if (replyTo != null) {
      Comment parentComment = commentRepository.findById(replyTo)
              .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", replyTo));
      depth = parentComment.getDepth() + 1;
    }
    // FIXME: need to check parent.collection.id == collectionId ?

    Comment comment = Comment.builder()
            .user(user)
            .collection(collection)
            .text(commentRequest.getText())
            .replyTo(commentRequest.getReplyTo())
            .depth(depth)
            .build();
    commentRepository.save(comment);
    return ModelMapper.map(comment, 0l);
  }

  @Transactional
  public CommentResponse updateCommentById(Long userId, Long commentId, CommentRequest commentRequest) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    if (!userId.equals(comment.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    comment.setText(commentRequest.getText());
    return ModelMapper.map(comment, null);
  }

  @Transactional
  public void removeCommentById(Long userId, Long commentId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    if (!userId.equals(comment.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    commentRepository.deleteAllByReplyTo(comment.getId());
    commentRepository.delete(comment);
  }

  public PagedResponse<CommentDetail> getCommentsByUser(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Comment> commentPage = commentRepository.findByUserId(userId, pageable);

    if (commentPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), commentPage);
    }
    List<CommentDetail> commentDetails = commentPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(commentDetails, commentPage);
  }

  public List<CommentResponse> getCommentsByCollection(Long collectionId, Long replyTo) {
    int depth = 0;
    if (replyTo != null) {
      Comment parentComment = commentRepository.findById(replyTo)
              .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", replyTo));
      depth = parentComment.getDepth() + 1;
    }

    // get reply counts of each comments and convert to Map
    // It manually gets counts via group-by query,
    // since comment-reply(comment-comment) relation isn't defined in model
    List<ReplyCount> replyCounts = commentRepository.findReplyCount(collectionId, depth + 1);
    HashMap<Long, Long> commentRepliesMap = new HashMap<>();
    replyCounts.forEach(replyCount -> commentRepliesMap.put(replyCount.getId(), replyCount.getReplies()));

    Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
    List<Comment> comments = commentRepository.findByCollectionIdAndDepthAndReplyTo(collectionId, depth, replyTo, sort);

    return comments.stream().map((comment) ->
            ModelMapper.map(comment, commentRepliesMap.getOrDefault(comment.getId(), 0l)))
            .collect(Collectors.toList());
  }
}