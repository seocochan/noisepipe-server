package com.noisepipe.server.utils;

import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.Comment;
import com.noisepipe.server.model.Item;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.*;

public class ModelMapper {

  // mapToSummary()
  public static UserSummary mapToSummary(User user) {
    return new UserSummary(user.getId(), user.getUsername(), user.getName());
  }

  public static CollectionSummary mapToSummary(Collection collection) {
    return new CollectionSummary(collection.getId(), collection.getTitle(), collection.getDescription(),
            ModelMapper.mapToSummary(collection.getUser()));
  }

  // map()
  public static CollectionResponse map(Collection collection) {
    return new CollectionResponse(collection.getId(), collection.getTitle(), collection.getDescription(),
            ModelMapper.mapToSummary(collection.getUser()));
  }

  public static CommentResponse map(Comment comment) {
    return new CommentResponse(comment.getId(), comment.getText(), comment.getDepth(),
            ModelMapper.mapToSummary(comment.getCollection()), ModelMapper.mapToSummary(comment.getUser()));
  }

  public static ItemResponse map(Item item) {
    return new ItemResponse(item.getId(), item.getTitle(), item.getDescription(), item.getSourceUrl(),
            item.getSourceProvider(), item.getStartAt(), item.getPosition(), item.getCreatedBy(),
            item.getCollection().getId());
  }
}