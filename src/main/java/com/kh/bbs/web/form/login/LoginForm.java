package com.kh.bbs.web.form.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
  @NotBlank(message = "이메일은 필수입력 사항입니다.")
  private String email;
  @NotBlank(message = "비밀번호는 필수입력 사항입니다.")
  private String passwd;
}
