package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.DateAudit;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Collection extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 40)
  private String title;

  @Size(max = 255)
  private String description;

  @OneToMany(
          mappedBy = "collection",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @BatchSize(size = 30)
  @Size(max = 100)
  @Builder.Default
  private List<Item> items = new ArrayList<>();

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(
          mappedBy = "collection",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(
          mappedBy = "collection",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @Builder.Default
  private List<Bookmark> bookmarks = new ArrayList<>();

  @ManyToMany(
          cascade = CascadeType.PERSIST,
          fetch = FetchType.LAZY
  )
  @JoinTable(name = "collections_tags",
          joinColumns = @JoinColumn(name = "collection_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id"),
          uniqueConstraints = {@UniqueConstraint(columnNames = {"collection_id", "tag_id"})}
  )
  @BatchSize(size = 30)
  @Size(max = 5)
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  public void addItem(Item item) {
    items.add(item);
    item.setCollection(this);
  }

  public void addTag(Tag tag) {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    tags.add(tag);
  }
}