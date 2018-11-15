package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "items") // FIXME: unique(collection_id & position)
@NoArgsConstructor
@Getter
@Setter
public class Item extends DateAudit {

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

  // @NotBlank
  private Double position;

  @ManyToOne(optional = false)
  @JoinColumn(name = "collection_id", nullable = false)
  private Collection collection;
}
