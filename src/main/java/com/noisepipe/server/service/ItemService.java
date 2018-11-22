package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.Item;
import com.noisepipe.server.payload.ItemRequest;
import com.noisepipe.server.payload.ItemResponse;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.repository.CollectionRepository;
import com.noisepipe.server.repository.ItemRepository;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final CollectionRepository collectionRepository;
  private final ItemRepository itemRepository;
  private final TagService tagService;

  @Transactional
  public void createItem(Long userId, Long collectionId, ItemRequest itemRequest) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    if (!userId.equals(collection.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    Item item = Item.builder()
            .collection(collection)
            .title(itemRequest.getTitle())
            .description(itemRequest.getDescription())
            .tags(tagService.getOrCreateTags(itemRequest.getTags()))
            .position(itemRequest.getPosition())
            .build();
    itemRepository.save(item);
  }

  @Transactional
  public void updateItemById(Long userId, Long itemId, ItemRequest itemRequest) {
    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
    if (!userId.equals(item.getCreatedBy())) {
      throw new BadRequestException("Permission denied");
    }

    item.setTitle(itemRequest.getTitle());
    item.setDescription(itemRequest.getDescription());
    item.setTags(tagService.getOrCreateTags(itemRequest.getTags()));
  }

  @Transactional
  public void updateItemPositionById(Long userId, Long itemId, Double position) {
    int updated = itemRepository.updatePosition(itemId, userId, position);
    if (updated == 0) { // no items're updated, not matched to id or owned by current user
      throw new BadRequestException("Permission denied");
    }
  }

  public void removeItemById(Long userId, Long itemId) {
    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
    if (!userId.equals(item.getCreatedBy())) {
      throw new BadRequestException("Permission denied");
    }

    itemRepository.delete(item);
  }

  public PagedResponse<ItemResponse> getItemsByCollection(Long collectionId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "position");
    Page<Item> itemPage = itemRepository.findByCollectionId(collectionId, pageable);

    if (itemPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), itemPage);
    }
    List<ItemResponse> itemResponses = itemPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(itemResponses, itemPage);
  }

  public PagedResponse<ItemResponse> getItemsByTagName(String tagName, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Item> itemPage = itemRepository.findByTagName(tagName, pageable);

    if (itemPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), itemPage);
    }
    List<ItemResponse> itemResponses = itemPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(itemResponses, itemPage);
  }
}