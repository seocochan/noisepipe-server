package com.noisepipe.server.utils;

public interface AppConstants {

  // common
  String YT_BASE_URL = "https://www.googleapis.com/youtube/v3";
  String SC_BASE_URL = "https://api.soundcloud.com";
  String DEFAULT_PAGE_NUMBER = "0";
  String DEFAULT_PAGE_SIZE = "12";
  int MAX_PAGE_SIZE = 50;
  int DEFAULT_BATCH_SIZE = 30;

  // models
  int MAX_COLLECTION_TITLE_LENGTH = 40;
  int MAX_COLLECTION_DESCRIPTION_LENGTH = 255;
  int MAX_COLLECTION_ITEMS_SIZE = 100;
  int MAX_COLLECTION_TAGS_SIZE = 5;
  int MAX_COMMENT_TEXT_LENGTH = 255;
  int MAX_CUE_NAME_LENGTH = 40;
  int MAX_CUE_SIZE = 20;
  int MAX_ITEM_TITLE_LENGTH = 100;
  int MAX_ITEM_DESCRIPTION_LENGTH = 255;
  int MAX_ITEM_TAGS_SIZE = 5;
  int MAX_TAG_NAME_LENGTH = 40;
  int MIN_NAME_LENGTH = 2;
  int MAX_NAME_LENGTH = 40;
  int MIN_USERNAME_LENGTH = 5;
  int MAX_USERNAME_LENGTH = 20;
  int MAX_EMAIL_LENGTH = 100;
  int MIN_PASSWORD_LENGTH = 8;
  int MAX_PASSWORD_LENGTH = 100;
}
