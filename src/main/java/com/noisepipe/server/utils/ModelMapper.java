package com.noisepipe.server.utils;

import com.noisepipe.server.model.Collection;
import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.payload.UserSummary;

public class ModelMapper {
  public static CollectionResponse map(Collection collection) {
    UserSummary userSummary = new UserSummary(
            collection.getUser().getId(), collection.getUser().getUsername(), collection.getUser().getName());
    return new CollectionResponse(collection.getId(), collection.getTitle(), collection.getDescription(), userSummary);
  }

  // overload 'map()' methods below...
}
