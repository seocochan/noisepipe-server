package com.noisepipe.server.controller;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.CommentRequest;
import com.noisepipe.server.payload.CommentResponse;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CommentService;
import com.noisepipe.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/collections/{collectionId}/comments")
@RequiredArgsConstructor
public class CollectionCommentsController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<ApiResponse> createCollection(@CurrentUser UserPrincipal currentUser,
                                                      @PathVariable Long collectionId,
                                                      @Valid @RequestBody CommentRequest commentRequest) {
    commentService.createComment(currentUser.toUser(), collectionId, commentRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a comment"));
  }

  @GetMapping
  public PagedResponse<CommentResponse> getCommentsByCollectionId(
          @PathVariable String collectionId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    Long _collectionId;
    try {
      _collectionId = Long.valueOf(collectionId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }

    return commentService.getCommentsByCollectionId(_collectionId, page, size);
  }
}