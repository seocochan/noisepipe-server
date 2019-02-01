package com.noisepipe.server.controller;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.payload.*;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CollectionService;
import com.noisepipe.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

  private final CollectionService collectionService;

  @GetMapping("/{collectionId}")
  public ResponseEntity<CollectionResponse> getCollectionById(@CurrentUser UserPrincipal currentUser,
                                                              @PathVariable Long collectionId) {
    Long userId = currentUser == null ? null : currentUser.getId();
    CollectionResponse collectionResponse = collectionService.getCollectionById(userId, collectionId);

    return ResponseEntity.ok(collectionResponse);
  }

  @PutMapping("/{collectionId}")
  public ResponseEntity<CollectionResponse> updateCollectionById(@CurrentUser UserPrincipal currentUser,
                                                                 @PathVariable Long collectionId,
                                                                 @Valid @RequestBody CollectionRequest collectionRequest) {
    return ResponseEntity.ok(collectionService.updateCollectionById(currentUser.getId(), collectionId, collectionRequest));
  }

  @DeleteMapping("/{collectionId}")
  public ResponseEntity<ApiResponse> removeCollectionById(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long collectionId) {
    collectionService.removeCollectionById(currentUser.getId(), collectionId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a collection"));
  }

  @GetMapping
  public PagedResponse<CollectionSummary> searchCollections(
          @RequestParam(required = false) String q,
          @RequestParam(required = false) String tagName,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    if (q == null && tagName == null) {
      throw new BadRequestException("Parameter q or tagName is required");
    }

    return tagName == null ? collectionService.searchCollections(q, page, size)
            : collectionService.getCollectionsByTagName(tagName, page, size);
  }
}