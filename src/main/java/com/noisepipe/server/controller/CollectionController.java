package com.noisepipe.server.controller;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.CollectionRequest;
import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

  @Autowired
  private CollectionService collectionService;

  private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);

  @PostMapping
  public ResponseEntity<ApiResponse> createCollection(@CurrentUser UserPrincipal currentUser,
                                                      @Valid @RequestBody CollectionRequest collectionRequest) {
    collectionService.createCollection(currentUser.getId(), collectionRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a collection"));
  }

  @GetMapping("/{collectionId}")
  public ResponseEntity<CollectionResponse> getCollectionById(@PathVariable String collectionId) {
    Long _collectionId;
    try {
      _collectionId = Long.valueOf(collectionId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }
    CollectionResponse collectionResponse = collectionService.getCollectionById(_collectionId);

    return ResponseEntity.ok(collectionResponse);
  }

  @PutMapping("{collectionId}")
  public ResponseEntity<ApiResponse> updateCollectionById(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable String collectionId,
                                                          @Valid @RequestBody CollectionRequest collectionRequest) {
    Long _collectionId;
    try {
      _collectionId = Long.valueOf(collectionId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }
    collectionService.updateCollectionById(currentUser.getId(), _collectionId, collectionRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a collection"));
  }

  @DeleteMapping("/{collectionId}")
  public ResponseEntity<ApiResponse> removeCollectionById(@CurrentUser UserPrincipal currentUser,
                                                          @PathVariable String collectionId) {
    Long _collectionId;
    try {
      _collectionId = Long.valueOf(collectionId);
    } catch (Exception e) {
      throw new BadRequestException("Invalid variable", e);
    }
    collectionService.removeCollectionById(currentUser.getId(), _collectionId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a collection"));
  }
}
