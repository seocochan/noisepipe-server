package com.noisepipe.server.utils;

import com.noisepipe.server.model.*;
import com.noisepipe.server.payload.*;

import java.util.Collections;
import java.util.stream.Collectors;

public class ModelMapper {

  // mapToSummary()
  public static UserSummary mapToSummary(User user) {
    return new UserSummary(user.getId(), user.getUsername());
  }

  public static CollectionSummary mapToSummary(Collection collection) {
    return CollectionSummary.builder()
            .id(collection.getId())
            .title(collection.getTitle())
            .items(collection.getItems().size())
            .tags(collection.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .createdBy(ModelMapper.mapToSummary(collection.getUser()))
            .createdAt(collection.getCreatedAt())
            .build();
  }

  // map()
  public static CollectionResponse map(Collection collection, Boolean isBookmarked) {
    return CollectionResponse.builder()
            .id(collection.getId())
            .title(collection.getTitle())
            .description(collection.getDescription())
            .items(collection.getItems().size())
            .comments(collection.getComments().size())
            .tags(collection.getTags() == null
                    ? Collections.emptyList()
                    : collection.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .bookmarks(collection.getBookmarks().size())
            .isBookmarked(isBookmarked)
            .createdBy(ModelMapper.mapToSummary(collection.getUser()))
            .createdAt(collection.getCreatedAt())
            .build();
  }

  public static CommentResponse map(Comment comment, Long replies) {
    return CommentResponse.builder()
            .id(comment.getId())
            .text(comment.getText())
            .depth(comment.getDepth())
            .replies(replies)
            .createdBy(ModelMapper.mapToSummary(comment.getUser()))
            .createdAt(comment.getCreatedAt())
            .build();
  }

  public static CommentSummary map(Comment comment) {
    return CommentSummary.builder()
            .id(comment.getId())
            .text(comment.getText())
            .collectionId(comment.getCollection().getId())
            .collectionTitle(comment.getCollection().getTitle())
            .createdBy(ModelMapper.mapToSummary(comment.getUser()))
            .createdAt(comment.getCreatedAt())
            .build();
  }

  public static ItemResponse map(Item item) {
    return ItemResponse.builder()
            .id(item.getId())
            .title(item.getTitle())
            .description(item.getDescription())
            .sourceUrl(item.getSourceUrl())
            .sourceProvider(item.getSourceProvider())
            .tags(item.getTags() == null
                    ? Collections.emptyList()
                    : item.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .position(item.getPosition())
            .createdBy(item.getCreatedBy())
            .collectionId(item.getCollection().getId())
            .createdAt(item.getCreatedAt())
            .updatedAt(item.getUpdatedAt())
            .build();
  }

  public static CueResponse map(Cue cue) {
    return CueResponse.builder()
            .id(cue.getId())
            .seconds(cue.getSeconds())
            .name(cue.getName())
            .createdAt(cue.getCreatedAt())
            .build();
  }
}