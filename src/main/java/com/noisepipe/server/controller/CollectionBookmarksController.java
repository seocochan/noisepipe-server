package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections/{collectionId}/bookmarks")
@RequiredArgsConstructor
public class CollectionBookmarksController {

  private final BookmarkService bookmarkService;

  @PostMapping
  public ResponseEntity<ApiResponse> createCollection(@CurrentUser UserPrincipal currentUser,
                                                      @PathVariable Long collectionId) {
    bookmarkService.createBookmark(currentUser.toUser(), collectionId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a bookmark"));
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse> removeBookmark(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long collectionId) {
    bookmarkService.removeBookmark(currentUser.getId(), collectionId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a bookmark"));
  }
}