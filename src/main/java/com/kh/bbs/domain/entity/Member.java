package com.kh.bbs.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Member {
  Long memberId;        // number(10),     --내부 관리 아이디
  String email;         // varchar2(50),   --로긴 아이디
  String passwd;        // varchar2(12),   --로긴 비밀번호
  String nickname;      // varchar2(30),   --별칭
  String gubun;         // varchar2(11)   default 'M0101', --회원구분 (일반,우수,관리자..)
  LocalDateTime cdate;  // timestamp default systimestamp,         --생성일시
  LocalDateTime udate;  // timestamp default systimestamp          --수정일시
}
