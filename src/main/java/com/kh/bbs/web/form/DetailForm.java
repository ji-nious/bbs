package com.kh.bbs.web.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetailForm {
  private String title;
  private String content;
  private String writer;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
