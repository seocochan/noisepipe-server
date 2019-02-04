package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.UserDateAudit;
import com.noisepipe.server.model.enums.Provider;
import com.noisepipe.server.utils.AppConstants;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = AppConstants.MAX_ITEM_TITLE_LENGTH)
  private String title;

  @Size(max = AppConstants.MAX_ITEM_DESCRIPTION_LENGTH)
  private String description;

  @NotBlank
  private String sourceUrl;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Provider sourceProvider;

  @NotNull
  private Double position;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "collection_id", nullable = false)
  private Collection collection;

  @ManyToMany(
          cascade = CascadeType.PERSIST,
          fetch = FetchType.LAZY
  )
  @JoinTable(name = "items_tags",
          joinColumns = @JoinColumn(name = "item_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id"),
          uniqueConstraints = {@UniqueConstraint(columnNames = {"item_id", "tag_id"})}
  )
  @BatchSize(size = AppConstants.DEFAULT_BATCH_SIZE)
  @Size(max = AppConstants.MAX_ITEM_TAGS_SIZE)
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  @OneToMany(
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @JoinColumn(name = "item_id")
  @BatchSize(size = AppConstants.DEFAULT_BATCH_SIZE)
  @OrderBy("seconds, id")
  @Builder.Default
  private List<Cue> cues = new ArrayList<>();

  public void addTag(Tag tag) {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    tags.add(tag);
  }

  public void addCue(Cue cue) {
    if (cues == null) {
      cues = new ArrayList<>();
    }
    cues.add(cue);
  }
}