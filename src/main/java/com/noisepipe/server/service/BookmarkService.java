package com.noisepipe.server.service;

import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Bookmark;
import com.noisepipe.server.model.Collection;
import com.noisepipe.server.model.User;
import com.noisepipe.server.payload.CollectionSummary;
import com.noisepipe.server.payload.PagedResponse;
import com.noisepipe.server.repository.BookmarkRepository;
import com.noisepipe.server.repository.CollectionRepository;
import com.noisepipe.server.repository.UserRepository;
import com.noisepipe.server.utils.ModelMapper;
import com.noisepipe.server.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

  private final UserRepository userRepository;
  private final CollectionRepository collectionRepository;
  private final BookmarkRepository bookmarkRepository;

  public void createBookmark(User user, Long collectionId) {
    // return 200 even bookmark already exists
    if (bookmarkRepository.existsByUserIdAndCollectionId(user.getId(), collectionId)) {
      return;
    }

    Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", collectionId));
    Bookmark bookmark = new Bookmark();
    bookmark.setUser(user);
    bookmark.setCollection(collection);
    bookmarkRepository.save(bookmark);
  }

  public void removeBookmark(Long userId, Long collectionId) {
    Bookmark bookmark = bookmarkRepository.findByUserIdAndCollectionId(userId, collectionId).orElse(null);
    // return 200 even bookmark not exists
    if (bookmark == null) return;
    bookmarkRepository.delete(bookmark);
  }

  public PagedResponse<CollectionSummary> getCollectionsBookmarkedByUser(String username, Long offsetId, int size) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    long offset = offsetId == null ? 0 : bookmarkRepository.getRownumById(user.getId(), offsetId)
            .orElseThrow(() -> new ResourceNotFoundException("Collection", "id", offsetId)).longValue();

    Pageable pageable = new OffsetBasedPageRequest(offset, size, Sort.Direction.DESC, "bookmarks.createdAt");
    Page<Collection> collectionPage = collectionRepository.findByBookmarksUserUsername(username, pageable);

    if (collectionPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), collectionPage);
    }
    List<CollectionSummary> collectionSummaries = collectionPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(collectionSummaries, collectionPage);
  }
}