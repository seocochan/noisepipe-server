package com.noisepipe.server.controller;

import com.noisepipe.server.payload.ApiResponse;
import com.noisepipe.server.payload.CueRequest;
import com.noisepipe.server.payload.CueResponse;
import com.noisepipe.server.security.CurrentUser;
import com.noisepipe.server.security.UserPrincipal;
import com.noisepipe.server.service.CueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cues")
@RequiredArgsConstructor
public class CueController {

  private final CueService cueService;

  @PutMapping("/{cueId}")
  public ResponseEntity<CueResponse> updateCueById(@CurrentUser UserPrincipal currentUser,
                                                   @PathVariable Long cueId,
                                                   @Valid @RequestBody CueRequest cueRequest) {
    return ResponseEntity.ok(cueService.updateCueById(currentUser.getId(), cueId, cueRequest));
  }

  @DeleteMapping("/{cueId}")
  public ResponseEntity<ApiResponse> removeCueById(@CurrentUser UserPrincipal currentUser,
                                                   @PathVariable Long cueId) {
    cueService.removeCueById(currentUser.getId(), cueId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a cue"));
  }
}
