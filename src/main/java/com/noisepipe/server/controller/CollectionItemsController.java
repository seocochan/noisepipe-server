package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.ItemPostRequest;
import com.noisepipe.server.payload.ItemResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/collections/{collectionId}/items")
@RequiredArgsConstructor
public class CollectionItemsController {

  private final ItemService itemService;

  @PostMapping
  public ResponseEntity<ItemResponse> createItem(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable Long collectionId,
                                                 @Valid @RequestBody ItemPostRequest itemPostRequest) {
    return ResponseEntity.ok(itemService.createItem(currentUser.getId(), collectionId, itemPostRequest));
  }

  @GetMapping
  public List<ItemResponse> getItemsByCollection(@PathVariable Long collectionId) {
    return itemService.getItemsByCollection(collectionId);
  }

  @PutMapping("/position")
  public ResponseEntity<ApiResponse> resetItemsPosition(@CurrentUser UserPrincipal currentUser,
                                                        @PathVariable Long collectionId) {
    itemService.resetItemsPosition(currentUser.getId(), collectionId);
    return ResponseEntity.ok(new ApiResponse(true, "Successfully reset items' position"));
  }
}