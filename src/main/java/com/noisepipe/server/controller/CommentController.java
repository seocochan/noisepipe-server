package com.noisepipe.server.controller;

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
                                                       @PathVariable Long commentId,
                                                       @Valid @RequestBody CommentRequest commentRequest) {
    commentService.updateCommentById(currentUser.getId(), commentId, commentRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a comment"));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<ApiResponse> removeCommentById(@CurrentUser UserPrincipal currentUser,
                                                       @PathVariable Long commentId) {
    commentService.removeCommentById(currentUser.getId(), commentId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a comment"));
  }
}