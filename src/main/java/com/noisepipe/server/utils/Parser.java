package com.noisepipe.server.utils;

import java.net.URL;

public class Parser {
  public static String getYoutubeId(URL url) {
    String host = url.getHost();
    String id = null;

    if (host.equals("youtu.be")) {
      id = url.getPath().substring(1);
    } else if (host.equals("www.youtube.com")) {
      for (String query : url.getQuery().split("&")) {
        if (query.startsWith("v=")) {
          id = query.substring(2);
        }
      }
    }
    return id;
  }
}
