package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.UserDateAudit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cues", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "item_id", "seconds"
        })
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cue extends UserDateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Integer seconds;

  @Size(max = 40)
  private String name;
}
