package com.kh.bbs.web.form.bbs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateForm {
  private Long boardId;

  @NotBlank(message = "제목은 필수입니다.")
  @Size(min=1, max=100, message = "제목은 100자를 초과할 수 없습니다.")
  private String title;

  @NotBlank(message = "내용은 필수입니다.")
  @Size(min=1, max=3000, message = "내용은 3000자를 초과할 수 없습니다.")
  private String content;

  @NotBlank(message = "작성자명은 필수입니다.")
  @Size(min=1, max=10, message = "작성자명은 100자를 초과할 수 없습니다.")
  private String writer;


  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
