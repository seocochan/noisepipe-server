package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.UserDateAudit;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "collection_id", "position"
        })
})
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
  @Size(max = 40)
  private String title;

  @Size(max = 255)
  private String description;

  @NotBlank
  private String sourceUrl;

  // @NotBlank
  private String sourceProvider;

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
  @BatchSize(size = 30)
  @Size(max = 5)
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  @OneToMany(
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @JoinColumn(name = "item_id")
  @OrderBy("seconds")
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