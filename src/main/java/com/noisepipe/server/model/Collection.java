package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
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
  @Size(max = 100)
  private List<Item> items = new ArrayList<>();

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(
          mappedBy = "collection",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(
          mappedBy = "collection",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  private List<Bookmark> bookmarks = new ArrayList<>();

  public void addItem(Item item) {
    items.add(item);
    item.setCollection(this);
  }
}
