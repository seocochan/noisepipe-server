package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.payload.media.Item;
import com.noisepipe.server.payload.media.Provider;
import com.noisepipe.server.payload.media.SoundcloudResponse;
import com.noisepipe.server.payload.media.YoutubeResponse;
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

  public String getMediaData(URL url, Provider provider) {
    String title;

    if (provider.equals(Provider.YOUTUBE)) {
      String id = Parser.getYoutubeId(url);
      String requestUrl = AppConstants.YT_BASE_URL + "/videos?id=" + id + "&key=" + YT_CLIENT_KEY + "&part=snippet&fields=items(snippet(title))";

      try {
        ResponseEntity<YoutubeResponse> response = restTemplate.getForEntity(requestUrl, YoutubeResponse.class);
        List<Item> items = response.getBody().getItems();
        if (items.size() == 0) {
          throw new ResourceNotFoundException("media", "URL", url.toString());
        }
        title = items.get(0).getSnippet().getTitle();
      } catch (Error e) {
        throw new BadRequestException("Bad Request");
      }
    } else { // SOUNDCLOUD
      String requestUrl = AppConstants.SC_BASE_URL + "/resolve.json?url=" + url + "&client_id=" + SC_CLIENT_KEY;

      try {
        ResponseEntity<SoundcloudResponse> response = restTemplate.getForEntity(requestUrl, SoundcloudResponse.class);
        title = response.getBody().getTitle();
      } catch (Error e) {
        throw new ResourceNotFoundException("media", "URL", url.toString());
      }
    }

    return title;
  }
}
