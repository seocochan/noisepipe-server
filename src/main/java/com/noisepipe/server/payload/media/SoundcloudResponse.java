package com.noisepipe.server.payload.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundcloudResponse {
  private String title;
  private String permalinkUrl;

  @JsonProperty("permalink_url")
  public void setPermalinkUrl(String permalinkUrl) {
    this.permalinkUrl = permalinkUrl;
  }
}
