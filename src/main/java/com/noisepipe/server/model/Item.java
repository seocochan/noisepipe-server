package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.UserDateAudit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items") // FIXME: unique(collection_id & position)
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

  // @NotBlank
  private String sourceUrl;

  // @NotBlank
  private String sourceProvider;

  private String startAt;

  @NotNull
  private Double position;

  @ManyToOne(optional = false)
  @JoinColumn(name = "collection_id", nullable = false)
  private Collection collection;

  @ManyToMany(
          mappedBy = "items",
          fetch = FetchType.LAZY
  )
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  public void addTag(Tag tag) {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    tags.add(tag);
  }
}