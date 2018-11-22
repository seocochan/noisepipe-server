package com.noisepipe.server.service;

import com.noisepipe.server.model.Tag;
import com.noisepipe.server.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  public List<Tag> getOrCreateTags(List<String> tagNames) {
    // when no tags are requested
    if (tagNames.size() == 0) {
      return null;
    }

    List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
    // when all the requested tags are not exist in DB
    if (existingTags.size() == 0) {
      return tagNames.stream().map(Tag::new).collect(Collectors.toList());
    }

    // when some of the requested tags are not exist in DB
    List<String> existingTagNames = existingTags.stream()
            .map(Tag::getName).collect(Collectors.toList());
    List<Tag> newTags = tagNames.stream()
            .filter(reqTagName -> !existingTagNames.contains(reqTagName))
            .map(Tag::new).collect(Collectors.toList());

    List<Tag> tags = new ArrayList<>();
    tags.addAll(existingTags);
    tags.addAll(newTags);
    return tags;
  }
}