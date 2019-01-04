package com.noisepipe.server.payload;

import com.noisepipe.server.utils.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequest {

  @NotBlank
  @Size(max = AppConstants.MAX_COMMENT_TEXT_LENGTH)
  private String text;

  private Long replyTo;
}
