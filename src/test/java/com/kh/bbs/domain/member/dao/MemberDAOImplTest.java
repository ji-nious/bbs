package com.kh.bbs.domain.member.dao;

import com.kh.bbs.domain.entity.Member;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@Slf4j
@SpringBootTest
@Transactional
class MemberDAOImplTest {

  @Autowired
  private MemberDAO memberDAO;


  @Test
  @DisplayName("회원가입")
  void insertMember() {
    //given
    Member member = new Member();
    member.setEmail("test3@kh.com");
    member.setPasswd("1234");
    member.setNickname("별칭3");
//    member.setGubun("M0101");

    //when
    Member insertedMember = memberDAO.insertMember(member);

    //then
    log.info("insertedMember={}", insertedMember);
  }

  @Test
  @DisplayName("회원존재유부By이메일")
  void isExist() {
    boolean exist = memberDAO.isExist("test1@kh.com");
    Assertions.assertThat(exist).isEqualTo(true);

    exist = memberDAO.isExist("test1@googld.com");
    Assertions.assertThat(exist).isEqualTo(false);
  }

  @Test
  @DisplayName("회원검색By회원아이디")
  void findByMemberId() {
    Optional<Member> optionalMember = memberDAO.findByMemberId(1L);

    if(optionalMember.isPresent()){
      Member member = optionalMember.get();
      log.info("member={}", member);
    }

    optionalMember = memberDAO.findByMemberId(0L);
    if(optionalMember.isEmpty()){
      log.info("회원없음");
    }
  }

  @Test
  @DisplayName("회원검색By이메일")
  void findByEmail() {
    Optional<Member> optionalMember = memberDAO.findByEmail("test1@kh.com");

    if(optionalMember.isPresent()){
      Member member = optionalMember.get();
      log.info("member={}", member);
    }

    optionalMember = memberDAO.findByEmail("test1@google.com");
    if(optionalMember.isEmpty()){
      log.info("회원없음");
    }
  }
}