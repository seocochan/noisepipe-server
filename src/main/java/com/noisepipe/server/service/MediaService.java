package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.payload.media.*;
import com.noisepipe.server.utils.AppConstants;
import com.noisepipe.server.utils.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

  private final RestTemplate restTemplate;

  @Value("${media.youtube.clientKey}")
  private String YT_CLIENT_KEY;
  @Value("${media.soundcloud.clientKey}")
  private String SC_CLIENT_KEY;

  public MediaDataResponse getMediaData(URL url, Provider provider) {
    if (provider.equals(Provider.YOUTUBE)) {
      String id = Parser.getYoutubeId(url);
      String requestUrl = AppConstants.YT_BASE_URL + "/videos?id=" + id + "&key=" + YT_CLIENT_KEY + "&part=snippet&fields=items(id,snippet(title))";

      try {
        ResponseEntity<YoutubeResponse> response = restTemplate.getForEntity(requestUrl, YoutubeResponse.class);
        List<Item> items = response.getBody().getItems();
        if (items.size() == 0) {
          throw new ResourceNotFoundException("media", "URL", url.toString());
        }
        String mediaUrl = "https://youtu.be/" + items.get(0).getId();
        String mediaTitle = items.get(0).getSnippet().getTitle();
        return new MediaDataResponse(mediaUrl, mediaTitle);
      } catch (Error e) {
        throw new BadRequestException("Bad Request");
      }
    } else { // SOUNDCLOUD
      String parsedUrl = Parser.convertSoundcloudUrl(url);
      String requestUrl = AppConstants.SC_BASE_URL + "/resolve.json?url=" + parsedUrl + "&client_id=" + SC_CLIENT_KEY;

      try {
        ResponseEntity<SoundcloudResponse> response = restTemplate.getForEntity(requestUrl, SoundcloudResponse.class);
        String mediaUrl = response.getBody().getPermalinkUrl();
        String mediaTitle = response.getBody().getTitle();
        return new MediaDataResponse(mediaUrl, mediaTitle);
      } catch (Error e) {
        throw new ResourceNotFoundException("media", "URL", url.toString());
      }
    }
  }
}
