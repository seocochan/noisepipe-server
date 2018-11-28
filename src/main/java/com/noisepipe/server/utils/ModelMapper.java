package com.noisepipe.server.utils;

import com.noisepipe.server.model.*;
import com.noisepipe.server.payload.*;

import java.util.stream.Collectors;

public class ModelMapper {

  // mapToSummary()
  public static UserSummary mapToSummary(User user) {
    return new UserSummary(user.getId(), user.getUsername(), user.getName());
  }

  public static CollectionSummary mapToSummary(Collection collection) {
    return CollectionSummary.builder()
            .id(collection.getId())
            .title(collection.getTitle())
            .description(collection.getDescription())
            .items(collection.getItems().size())
            .createdBy(ModelMapper.mapToSummary(collection.getUser()))
            .build();
  }

  // map()
  public static CollectionResponse map(Collection collection, Boolean isBookmarked) {
    return CollectionResponse.builder()
            .id(collection.getId())
            .title(collection.getTitle())
            .description(collection.getTitle())
            .createdBy(ModelMapper.mapToSummary(collection.getUser()))
            .tags(collection.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .bookmarks(collection.getBookmarks().size())
            .isBookmarked(isBookmarked)
            .build();
  }

  public static CommentResponse map(Comment comment) {
    return new CommentResponse(comment.getId(), comment.getText(), comment.getDepth(),
            ModelMapper.mapToSummary(comment.getCollection()), ModelMapper.mapToSummary(comment.getUser()));
  }

  public static ItemResponse map(Item item) {
    return ItemResponse.builder()
            .id(item.getId())
            .title(item.getTitle())
            .description(item.getDescription())
            .sourceUrl(item.getSourceUrl())
            .sourceProvider(item.getSourceProvider())
            .startAt(item.getStartAt())
            .tags(item.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .position(item.getPosition())
            .createdBy(item.getCreatedBy())
            .collectionId(item.getCollection().getId())
            .createdAt(item.getCreatedAt())
            .updatedAt(item.getUpdatedAt())
            .build();
  }
}