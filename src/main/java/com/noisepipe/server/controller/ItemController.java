package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.ItemRequest;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.ItemService;
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
  public ResponseEntity<ApiResponse> updateItemById(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long itemId,
                                                    @Valid @RequestBody ItemRequest itemRequest) {
    itemService.updateItemById(currentUser.getId(), itemId, itemRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a item"));
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
}