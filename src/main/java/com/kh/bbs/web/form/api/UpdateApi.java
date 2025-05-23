package com.kh.bbs.web.form.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateApi {

  @NotBlank(message = "내용은 필수 입력사항입니다.")
  @Size(min=1, max=1000, message = "내용은 최대 1000자를 넘을 수 없습니다.")
  private String content;
}
