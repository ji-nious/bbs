package com.kh.bbs.web.form.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinForm {
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String email;             // varchar2(50),      --로긴 아이디

  @NotBlank(message = "비밀번호은 필수 입력값입니다.")
  @Pattern(regexp = "^\\d{4,8}$", message = "비밀번호는 숫자로만 4~8자리여야 합니다.")
  private String passwd;            // varchar2(12),      --로긴 비밀번호

  @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
  private String passwdChk;         // varchar2(12),      --로긴 비밀번호 확인

  @NotBlank(message = "닉네임은 필수 입력값입니다.")
  @Pattern(
      regexp = "^([가-힣]{1,10}|[a-zA-Z]{1,15})$",
      message = "닉네임은 한글 10자 이하 또는 영어 15자 이하로 입력하세요."
  )
  private String nickname;          // varchar2(30),      --별칭
}
