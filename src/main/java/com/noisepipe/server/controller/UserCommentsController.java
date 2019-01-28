package com.noisepipe.server.controller;

import com.noisepipe.server.payload.CommentSummary;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.service.CommentService;
import com.noisepipe.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{username}/comments")
@RequiredArgsConstructor
public class UserCommentsController {

  private final CommentService commentService;

  @GetMapping
  public PagedResponse<CommentSummary> getCommentsByUser(
          @PathVariable String username,
          @RequestParam(required = false) Long offsetId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return commentService.getCommentsByUser(username, offsetId, size);
  }
}