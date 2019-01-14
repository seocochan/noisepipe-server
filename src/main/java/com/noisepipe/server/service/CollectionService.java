package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.CollectionRequest;
import com.noisepipe.server.payload.CollectionResponse;
import com.noisepipe.server.payload.CollectionSummary;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.repository.BookmarkRepository;
import com.noisepipe.server.repository.CollectionRepository;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

  private final CollectionRepository collectionRepository;
  private final BookmarkRepository bookmarkRepository;
  private final TagService tagService;

  @Transactional
  public void createCollection(User user, CollectionRequest collectionRequest) {
    Collection collection = Collection.builder()
            .user(user)
            .title(collectionRequest.getTitle())
            .description(collectionRequest.getDescription())
            .tags(tagService.getOrCreateTags(collectionRequest.getTags()))
            .build();

    collectionRepository.save(collection);
  }

  public CollectionResponse getCollectionById(Long userId, Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    Boolean isBookmarked = userId == null ? false
            : bookmarkRepository.existsByUserIdAndCollectionId(userId, collectionId);

    return ModelMapper.map(collection, isBookmarked);
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
    collection.setTags(tagService.getOrCreateTags(collectionRequest.getTags()));
  }

  public void removeCollectionById(Long userId, Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    if (!userId.equals(collection.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    collectionRepository.delete(collection);
  }

  public PagedResponse<CollectionSummary> getCollectionsByUser(String username, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Collection> collectionPage = collectionRepository.findByUserUsername(username, pageable);

    if (collectionPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), collectionPage);
    }
    List<CollectionSummary> collectionResponses = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionResponses, collectionPage);
  }

  public PagedResponse<CollectionSummary> getCollectionsByTagName(String tagName, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Collection> collectionPage = collectionRepository.findByTagName(tagName, pageable);

    if (collectionPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), collectionPage);
    }
    List<CollectionSummary> collectionResponses = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionResponses, collectionPage);
  }
}