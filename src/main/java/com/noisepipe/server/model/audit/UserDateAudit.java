package com.noisepipe.server.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdBy", "updatedBy"},
        allowGetters = true
)
@Getter
@Setter
public class UserDateAudit extends DateAudit {

  @CreatedBy
  @Column
  private Long createdBy;

  @LastModifiedBy
  private Long updateBy;
}
