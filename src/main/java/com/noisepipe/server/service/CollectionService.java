package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.CollectionRequest;
import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.repository.CollectionRepository;
import com.noisepipe.server.repository.UserRepository;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollectionService {

  private final UserRepository userRepository;
  private final CollectionRepository collectionRepository;

  @Transactional
  public void createCollection(Long userId, CollectionRequest collectionRequest) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    Collection collection = Collection.builder()
            .title(collectionRequest.getTitle())
            .description(collectionRequest.getDescription())
            .build();
    user.addCollection(collection);
  }

  public CollectionResponse getCollectionById(Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));

    return ModelMapper.map(collection);
  }

  @Transactional
  public void updateCollectionById(Long userId, Long collectionId, CollectionRequest collectionRequest) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    if (!userId.equals(collection.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    collection.setTitle(collectionRequest.getTitle());
    collection.setDescription(collectionRequest.getDescription());
  }

  public void removeCollectionById(Long userId, Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    if (!userId.equals(collection.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    collectionRepository.delete(collection);
  }
}
