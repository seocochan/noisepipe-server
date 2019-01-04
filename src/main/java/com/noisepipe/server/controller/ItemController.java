package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.ItemPutRequest;
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
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @PutMapping("/{itemId}")
  public ResponseEntity<ItemResponse> updateItemById(@CurrentUser UserPrincipal currentUser,
                                                     @PathVariable Long itemId,
                                                     @Valid @RequestBody ItemPutRequest itemPutRequest) {
    return ResponseEntity.ok(itemService.updateItemById(currentUser.getId(), itemId, itemPutRequest));
  }

  @PutMapping("/{itemId}/position")
  public ResponseEntity<ApiResponse> updateItemPositionById(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable Long itemId,
                                                            @RequestBody Double position) {
    itemService.updateItemPositionById(currentUser.getId(), itemId, position);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a item's position"));
  }

  @DeleteMapping("/{itemId}")
  public ResponseEntity<ApiResponse> removeItemById(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long itemId) {
    itemService.removeItemById(currentUser.getId(), itemId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a item"));
  }

  @GetMapping
  public PagedResponse<ItemResponse> getItemsByTagName(
          @RequestParam String tagName,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return itemService.getItemsByTagName(tagName, page, size);
  }
}