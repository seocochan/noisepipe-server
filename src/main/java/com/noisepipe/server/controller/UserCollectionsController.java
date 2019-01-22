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
@RequestMapping("/api/users/{username}/collections")
@RequiredArgsConstructor
public class UserCollectionsController {

  private final CollectionService collectionService;

  @PostMapping
  public ResponseEntity<CollectionResponse> createCollection(@CurrentUser UserPrincipal currentUser,
                                                             @PathVariable String username,
                                                             @Valid @RequestBody CollectionRequest collectionRequest) {
    if (!username.equals(currentUser.getUsername())) {
      throw new BadRequestException("Permission denied");
    }
    return ResponseEntity.ok(collectionService.createCollection(currentUser.toUser(), collectionRequest));
  }

  @GetMapping
  public PagedResponse<CollectionSummary> getCollectionsByUser(
          @PathVariable String username,
          @RequestParam(required = false) Long offsetId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return collectionService.getCollectionsByUser(username, offsetId, size);
  }
}