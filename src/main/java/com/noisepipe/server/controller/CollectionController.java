package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.CollectionRequest;
import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CollectionService;
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
  public ResponseEntity<ApiResponse> updateCollectionById(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long collectionId,
                                                          @Valid @RequestBody CollectionRequest collectionRequest) {
    collectionService.updateCollectionById(currentUser.getId(), collectionId, collectionRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a collection"));
  }

  @DeleteMapping("/{collectionId}")
  public ResponseEntity<ApiResponse> removeCollectionById(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable Long collectionId) {
    collectionService.removeCollectionById(currentUser.getId(), collectionId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a collection"));
  }
}