package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.Comment;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.CommentRequest;
import com.noisepipe.server.payload.CommentResponse;
import com.noisepipe.server.payload.PagedResponse;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CollectionRepository collectionRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public void createComment(User user, Long collectionId, CommentRequest commentRequest) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));

    Comment comment = Comment.builder()
            .user(user)
            .collection(collection)
            .text(commentRequest.getText())
            .replyTo(commentRequest.getReplyTo())
            .depth(0)
            .build();
    commentRepository.save(comment);
  }

  @Transactional
  public void updateCommentById(Long userId, Long commentId, CommentRequest commentRequest) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    if (!userId.equals(comment.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    comment.setText(commentRequest.getText());
  }

  public void removeCommentById(Long userId, Long commentId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    if (!userId.equals(comment.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    commentRepository.delete(comment);
  }

  public PagedResponse<CommentResponse> getCommentsByUser(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Comment> commentPage = commentRepository.findByUserId(userId, pageable);

    if (commentPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), commentPage);
    }
    List<CommentResponse> commentResponses = commentPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(commentResponses, commentPage);
  }

  public PagedResponse<CommentResponse> getCommentsByCollection(Long collectionId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Page<Comment> commentPage = commentRepository.findByCollectionId(collectionId, pageable);

    if (commentPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), commentPage);
    }
    List<CommentResponse> commentResponses = commentPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(commentResponses, commentPage);
  }
}