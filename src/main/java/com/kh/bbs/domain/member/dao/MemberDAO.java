package com.kh.bbs.domain.member.dao;

import com.kh.bbs.domain.entity.Member;

import java.util.Optional;


public interface MemberDAO {

  /**
   * 회원가입
   * @param member 회원정보
   * @return 가입 후 회원정보
   */
  Member insertMember(Member member);

  /**
   * 회원 존재 유부
   * @param email
   * @return
   */
  boolean isExist(String email);

  /**
   * 회원 조회 by memberId
   * @param memberId
   * @return 회원정보
   */
  Optional<Member> findByMemberId(Long memberId);

  /**
   * 회원 조회 by eamil
   * @param email
   * @return 회원정보
   */
  Optional<Member> findByEmail(String email);



}
