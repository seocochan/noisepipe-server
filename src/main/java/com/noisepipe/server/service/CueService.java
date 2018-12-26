package com.noisepipe.server.service;

import com.noisepipe.server.exception.BadRequestException;
import com.noisepipe.server.exception.ResourceNotFoundException;
import com.noisepipe.server.model.Cue;
import com.noisepipe.server.model.Item;
import com.noisepipe.server.payload.CueRequest;
import com.noisepipe.server.payload.CueResponse;
import com.noisepipe.server.repository.CueRepository;
import com.noisepipe.server.repository.ItemRepository;
import com.noisepipe.server.utils.AppConstants;
import com.noisepipe.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CueService {

  private final ItemRepository itemRepository;
  private final CueRepository cueRepository;

  @Transactional
  public void createCue(Long userId, Long itemId, CueRequest cueRequest) {
    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
    if (!userId.equals(item.getCreatedBy())) {
      throw new BadRequestException("Permission denied");
    }
    if (item.getCues().size() >= AppConstants.MAX_CUE_SIZE) {
      throw new BadRequestException("Too many cues");
    }

    Cue cue = Cue.builder()
            .seconds(cueRequest.getSeconds())
            .name(cueRequest.getName())
            .build();
    item.addCue(cue);
  }

  @Transactional
  public void updateCueById(Long userId, Long cueId, CueRequest cueRequest) {
    Cue cue = cueRepository.findById(cueId)
            .orElseThrow(() -> new ResourceNotFoundException("Cue", "id", cueId));
    if (!userId.equals(cue.getCreatedBy())) {
      throw new BadRequestException("Permission denied");
    }

    cue.setSeconds(cueRequest.getSeconds());
    cue.setName(cueRequest.getName());
  }

  public void removeCueById(Long userId, Long cueId) {
    Cue cue = cueRepository.findById(cueId)
            .orElseThrow(() -> new ResourceNotFoundException("Cue", "id", cueId));
    if (!userId.equals(cue.getCreatedBy())) {
      throw new BadRequestException("Permission denied");
    }

    cueRepository.delete(cue);
  }

  public List<CueResponse> getCuesByItem(Long itemId) {
    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
    return item.getCues().stream().map(ModelMapper::map).collect(Collectors.toList());
  }
}
