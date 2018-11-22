package com.noisepipe.server.controller;

import com.noisepipe.server.payload.CollectionSummary;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.service.BookmarkService;
import com.noisepipe.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/bookmarks")
@RequiredArgsConstructor
public class UserBookmarksController {

  private final BookmarkService bookmarkService;

  @GetMapping
  public PagedResponse<CollectionSummary> getCollectionsBookmarkedByUser(
          @PathVariable Long userId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return bookmarkService.getCollectionsBookmarkedByUser(userId, page, size);
  }
}