package com.noisepipe.server.controller;

import com.noisepipe.server.payload.CommentRequest;
import com.noisepipe.server.payload.CommentResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/collections/{collectionId}/comments")
@RequiredArgsConstructor
public class CollectionCommentsController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<CommentResponse> createComment(@CurrentUser UserPrincipal currentUser,
                                                       @PathVariable Long collectionId,
                                                       @Valid @RequestBody CommentRequest commentRequest) {
    return ResponseEntity.ok(commentService.createComment(currentUser.toUser(), collectionId, commentRequest));
  }

  @GetMapping
  public List<CommentResponse> getCommentsByCollection(@PathVariable Long collectionId) {
    return commentService.getCommentsByCollection(collectionId, null);
  }

  @GetMapping("/{commentId}/replies")
  public List<CommentResponse> getCommentReplies(@PathVariable Long collectionId, @PathVariable Long commentId) {
    return commentService.getCommentsByCollection(collectionId, commentId);
  }
}