package com.noisepipe.server.controller;

import com.noisepipe.server.payload.CueRequest;
import com.noisepipe.server.payload.CueResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items/{itemId}/cues")
@RequiredArgsConstructor
public class ItemCuesController {

  private final CueService cueService;

  @PostMapping
  public ResponseEntity<CueResponse> createCue(@CurrentUser UserPrincipal currentUser,
                                               @PathVariable Long itemId,
                                               @Valid @RequestBody CueRequest cueRequest) {
    return ResponseEntity.ok(cueService.createCue(currentUser.getId(), itemId, cueRequest));
  }

  @GetMapping
  public List<CueResponse> getCuesByItem(@PathVariable Long itemId) {
    return cueService.getCuesByItem(itemId);
  }
}
