package com.noisepipe.server.model;

import com.noisepipe.server.model.audit.DateAudit;
import com.noisepipe.server.utils.AppConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = AppConstants.MAX_COMMENT_TEXT_LENGTH)
  private String text;

  private Long replyTo;

  @NotNull
  @Min(0)
  @Max(1)
  private Integer depth;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "collection_id", nullable = false)
  private Collection collection;
}
