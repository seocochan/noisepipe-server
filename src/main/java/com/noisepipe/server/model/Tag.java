package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.DateAudit;
import com.noisepipe.server.utils.AppConstants;
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
  @Size(max = AppConstants.MAX_TAG_NAME_LENGTH)
  private String name;

  @ManyToMany(
          mappedBy = "tags",
          fetch = FetchType.LAZY
  )
  private List<Collection> collections = new ArrayList<>();

  @ManyToMany(
          mappedBy = "tags",
          fetch = FetchType.LAZY
  )
  private List<Item> items = new ArrayList<>();

  public Tag(String name) {
    this.name = name;
  }
}