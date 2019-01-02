package com.noisepipe.server.controller;

import com.noisepipe.server.payload.media.MediaDataResponse;
import com.noisepipe.server.model.enums.Provider;
import com.noisepipe.server.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

  private final MediaService mediaService;

  @GetMapping
  public ResponseEntity<MediaDataResponse> getMediaData(@RequestParam URL url, @RequestParam Provider provider) {
    return ResponseEntity.ok(mediaService.getMediaData(url, provider));
  }
}
