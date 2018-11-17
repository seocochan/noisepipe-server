package com.noisepipe.server.controller;

import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{username}/collections")
@RequiredArgsConstructor
public class UserCollectionsController {

  private final CollectionService collectionService;

  @GetMapping
  public PagedResponse<CollectionResponse> getCollectionsByUsername(@PathVariable String username,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size) {
    return collectionService.getCollectionsByUsername(username, page, size);
  }
}
