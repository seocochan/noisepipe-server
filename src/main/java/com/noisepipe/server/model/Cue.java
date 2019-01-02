package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.UserDateAudit;
import com.noisepipe.server.utils.AppConstants;
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

  @Size(max = AppConstants.MAX_CUE_NAME_LENGTH)
  private String name;
}
