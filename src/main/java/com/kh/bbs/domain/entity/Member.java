package com.kh.bbs.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Member {
  private Long memberId;        // number(10),     --내부 관리 아이디
  private String email;         // varchar2(50),   --로긴 아이디
  private String passwd;        // varchar2(12),   --로긴 비밀번호
  private String nickname;      // varchar2(30),   --별칭
  private String gubun;         // varchar2(11)   default 'M0101', --회원구분 (일반,우수,관리자..)
  private LocalDateTime cdate;  // timestamp default systimestamp,         --생성일시
  private LocalDateTime udate;  // timestamp default systimestamp          --수정일시
}
