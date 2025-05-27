package com.kh.bbs.domain.member.dao;

import com.kh.bbs.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO{

  private final NamedParameterJdbcTemplate template;

  @Override
  public Member insertMember(Member member) {
    // 1) sql 준비
    StringBuffer sql = new StringBuffer();
    sql.append("insert into member (member_id, email, passwd, nickname) ");
    sql.append("values(member_member_id_seq.nextval, :email, :passwd, :nickname) ");

    // 2) 객체 필드명과 sql 파라미터명 자동 매핑(데이터들이 찾아갈 지도 만들기)
    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(member);

    // 3) GeneratedKeyholder()로 insert 실행 후 DB에서 자동생성된 키(pk/seq) 반환받을 변수 준비
    KeyHolder keyHolder = new GeneratedKeyHolder();

    // 4) 객체에 담긴 값 DB에 전달하기
    template.update(sql.toString(), param, keyHolder, new String[]{"member_id"});

    // 5) memberId Long 값으로 받기
    long memberId = ((Number) keyHolder.getKeys().get("member_id")).longValue();

    return findByMemberId(memberId).get();  //가입 후 회원정보
  }


  @Override
  public boolean isExist(String email) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT count(*) ");
    sql.append("FROM MEMBER ");
    sql.append("WHERE email = :email ");

    //mapping
    Map<String, Object> param = Map.of("email", email);

    //DB값 자바에 전달
    Integer cntOfRec = template.queryForObject(sql.toString(), param, Integer.class);

    return (cntOfRec == 1) ? true : false;
  }


  @Override
  public Optional<Member> findByMemberId(Long memberId) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT member_id, email, passwd, nickname ");
    sql.append("FROM MEMBER ");
    sql.append("WHERE member_id = :memberId ");

    //mapping
    Map<String, Long> param = Map.of("memberId", memberId);

    //DB->JAVA
    try {
      Member member = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(Member.class));
      return Optional.of(member);

    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT member_id, email, passwd, nickname ");
    sql.append("FROM MEMBER ");
    sql.append("WHERE email = :email ");

    //mapping
    Map<String, String> param = Map.of("email", email);

    //DB->JAVA
    try {
      Member member = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(Member.class));
      return Optional.of(member);

    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }


  }
}
