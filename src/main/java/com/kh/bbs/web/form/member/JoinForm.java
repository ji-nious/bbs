package com.kh.bbs.web.form.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinForm {
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",message = "이메일 형식에 맞지 않습니다.")
  @Size(min=7,max=50,message = "이메일 크기는 7자~50자 사이 여야합니다.")
  private String email;             // varchar2(50),      --로긴 아이디
  @NotBlank(message = "비밀번호은 필수 입력값입니다.")
  private String passwd;            // varchar2(12),      --로긴 비밀번호
  @NotBlank(message = "비밀번호확인은 필수 입력값입니다.")
  private String passwdChk;         // varchar2(12),      --로긴 비밀번호 확인
  private String nickname;          // varchar2(30),      --별칭
}
