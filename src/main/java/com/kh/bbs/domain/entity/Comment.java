package com.kh.bbs.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
  private Long commentsId;
  private String content;
  private String writer;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
