package com.noisepipe.server.controller;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.CommentRequest;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PutMapping("/{commentId}")
  public ResponseEntity<ApiResponse> updateCommentById(@CurrentUser UserPrincipal currentUser,
                                                       @PathVariable String commentId,
                                                       @Valid @RequestBody CommentRequest commentRequest) {
    Long _commentId;
    try {
      _commentId = Long.valueOf(commentId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }
    commentService.updateCommentById(currentUser.getId(), _commentId, commentRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a comment"));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<ApiResponse> updateCommentById(@CurrentUser UserPrincipal currentUser,
                                                       @PathVariable String commentId) {
    Long _commentId;
    try {
      _commentId = Long.valueOf(commentId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }
    commentService.removeCommentById(currentUser.getId(), _commentId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a comment"));
  }
}