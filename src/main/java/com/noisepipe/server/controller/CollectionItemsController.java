package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.ItemRequest;
import com.noisepipe.server.payload.ItemResponse;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.ItemService;
import com.noisepipe.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/collections/{collectionId}/items")
@RequiredArgsConstructor
public class CollectionItemsController {

  private final ItemService itemService;

  @PostMapping
  public ResponseEntity<ApiResponse> createItem(@CurrentUser UserPrincipal currentUser,
                                                @PathVariable Long collectionId,
                                                @Valid @RequestBody ItemRequest itemRequest) {
    itemService.createItem(currentUser.getId(), collectionId, itemRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a item"));
  }

  @GetMapping
  public PagedResponse<ItemResponse> getItemsByCollection(
          @PathVariable Long collectionId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return itemService.getItemsByCollection(collectionId, page, size);
  }
}