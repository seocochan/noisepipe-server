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
import com.noisepipe.server.repository.ItemRepository;
import com.noisepipe.server.repository.UserRepository;
import com.noisepipe.server.utils.ModelMapper;
import com.noisepipe.server.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {

  private final UserRepository userRepository;
  private final CollectionRepository collectionRepository;
  private final ItemRepository itemRepository;
  private final BookmarkRepository bookmarkRepository;
  private final TagService tagService;

  @Transactional
  public CollectionResponse createCollection(User user, CollectionRequest collectionRequest) {
    Collection collection = Collection.builder()
            .user(user)
            .title(collectionRequest.getTitle())
            .description(collectionRequest.getDescription())
            .tags(tagService.getOrCreateTags(collectionRequest.getTags()))
            .build();

    return ModelMapper.map(collectionRepository.save(collection), false);
  }

  public CollectionResponse getCollectionById(Long userId, Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    Boolean isBookmarked = userId == null ? false
            : bookmarkRepository.existsByUserIdAndCollectionId(userId, collectionId);

    return ModelMapper.map(collection, isBookmarked);
  }

  @Transactional
  public CollectionResponse updateCollectionById(Long userId, Long collectionId, CollectionRequest collectionRequest) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    if (!userId.equals(collection.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    collection.setTitle(collectionRequest.getTitle());
    collection.setDescription(collectionRequest.getDescription());
    collection.setTags(tagService.getOrCreateTags(collectionRequest.getTags()));
    Boolean isBookmarked = userId == null ? false
            : bookmarkRepository.existsByUserIdAndCollectionId(userId, collectionId);

    return ModelMapper.map(collection, isBookmarked);
  }

  public void removeCollectionById(Long userId, Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    if (!userId.equals(collection.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    collectionRepository.delete(collection);
  }

  public PagedResponse<CollectionSummary> getCollectionsByUser(String username, Long offsetId, int size) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    long offset = offsetId == null ? 0 : collectionRepository.getRownumById(user.getId(), offsetId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", offsetId)).longValue();

    Pageable pageable = new OffsetBasedPageRequest(offset, size, Sort.Direction.DESC, "createdAt");
    Page<Collection> collectionPage = collectionRepository.findByUserUsername(username, pageable);

    if (collectionPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), collectionPage);
    }
    List<CollectionSummary> collectionSummaries = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionSummaries, collectionPage);
  }

  public PagedResponse<CollectionSummary> getCollectionsByTagName(String tagName, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Collection> collectionPage = collectionRepository.findByTagName(tagName, pageable);

    if (collectionPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), collectionPage);
    }
    List<CollectionSummary> collectionSummaries = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionSummaries, collectionPage);
  }

  public PagedResponse<CollectionSummary> searchCollections(String q, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Collection> collectionPage
            = collectionRepository.findDistinctByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(q, q, pageable);

    if (collectionPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), collectionPage);
    }
    List<CollectionSummary> collectionSummaries = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionSummaries, collectionPage);
  }

  public List<CollectionSummary> getRecentlyCreatedCollections() {
    return collectionRepository.findTop6By(Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream().map(ModelMapper::mapToSummary).collect(Collectors.toList());
  }

  public List<CollectionSummary> getRecentlyUpdatedCollections() {
    List<Long> collectionIds = itemRepository.findRecentlyUpdatedCollectionIds(PageRequest.of(0, 6));
    return collectionRepository.findByIdInOrderByField(collectionIds)
            .stream().map(ModelMapper::mapToSummary).collect(Collectors.toList());
  }
}