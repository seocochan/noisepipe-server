package com.noisepipe.server.controller;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.CollectionRequest;
import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.payload.PagedResponse;
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
  public ResponseEntity<ApiResponse> createCollection(@CurrentUser UserPrincipal currentUser,
                                                      @PathVariable String username,
                                                      @Valid @RequestBody CollectionRequest collectionRequest) {
    if (!username.equals(currentUser.getUsername())) {
      throw new BadRequestException("Permission denied");
    }
    collectionService.createCollection(currentUser.toUser(), collectionRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a collection"));
  }

  @GetMapping
  public PagedResponse<CollectionResponse> getCollectionsByUsername(
          @PathVariable String username,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return collectionService.getCollectionsByUsername(username, page, size);
  }
}