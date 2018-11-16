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
@Table(name = "tags", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        })
})
@NoArgsConstructor
@Getter
@Setter
public class Tag extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 40)
  private String name;

  @ManyToMany
  @JoinTable(name = "tags_collections",
          joinColumns = @JoinColumn(name = "tag_id"),
          inverseJoinColumns = @JoinColumn(name = "collection_id"),
          uniqueConstraints = {@UniqueConstraint(columnNames = {"tag_id", "collection_id"})}
  )
  private List<Collection> collections = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "tags_items",
          joinColumns = @JoinColumn(name = "tag_id"),
          inverseJoinColumns = @JoinColumn(name = "item_id"),
          uniqueConstraints = {@UniqueConstraint(columnNames = {"tag_id", "item_id"})}
  )
  private List<Item> items = new ArrayList<>();
}
