package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Bookmark;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.CollectionSummary;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.repository.BookmarkRepository;
import com.noisepipe.server.repository.CollectionRepository;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

  private final CollectionRepository collectionRepository;
  private final BookmarkRepository bookmarkRepository;

  public void createBookmark(User user, Long collectionId) {
    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));

    Bookmark bookmark = new Bookmark();
    bookmark.setUser(user);
    bookmark.setCollection(collection);
    bookmarkRepository.save(bookmark);
  }

  public void removeBookmark(Long userId, Long collectionId) {
    Bookmark bookmark = bookmarkRepository.findByUserIdAndCollectionId(userId, collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmark", "collection_id", collectionId));
    if (!userId.equals(bookmark.getUser().getId())) { // FIXME: needless check?
      throw new BadRequestException("Permission denied");
    }

    bookmarkRepository.delete(bookmark);
  }

  public PagedResponse<CollectionSummary> getCollectionsBookmarkedByUser(Long userId, int page, int size) {
    List<Long> collectionIds = bookmarkRepository.findAllCollectionIds(userId);
    if (collectionIds.size() == 0) {
      return new PagedResponse<>(Collections.emptyList(), page, size, 0l, 0, true);
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<Collection> collectionPage = collectionRepository.findByIdInOrderByField(collectionIds, pageable);

    List<CollectionSummary> collectionSummaries = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionSummaries, collectionPage);
  }
}