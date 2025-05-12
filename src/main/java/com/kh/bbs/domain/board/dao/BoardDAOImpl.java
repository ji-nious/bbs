package com.kh.bbs.domain.board.dao;

import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Slf4j  //로깅하기 위한 어노테이션
@RequiredArgsConstructor
//생성자 생성. BoardDAOImpl 클래스는 데이터베이스와 상호작용하기 위해서 NamedParameterJdbcTemplate 같은 객체를 주입받아야 한다.
//이 객채를 new로 새로 생성하기보다 자동으로 생성자를 생성할 때에 주입 받는다.
//final 필드로 template를 지정하게 되므로 requiredArgsconstructor를 사용한다.
@Repository //DAO 클래스임을 명시
public class BoardDAOImpl implements BoardDAO{

  final NamedParameterJdbcTemplate template; //Entity Board 객체 필드와 SQL 내 파라미터 연결도구

  /**
   * 게시판 글쓰기
   * @param board
   * @return
   */
  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO board (board_id, title, content, writer) ");
    sql.append("     VALUES (board_board_id_seq.nextval, :title , :content, :writer) ");

    //자바객체 필드명을 자동으로 파라미터명으로 인식. 해당 값을 SQL 쿼리의 파라미터에 자동으로 바인딩. Map 형태로 저장.
    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

    //INSERT, UPDATE, DELETE 쿼리에서 영향받은 행의 수(int)를 반환
    long rows = template.update(sql.toString(), param);
    log.info("삽입된 행의 수 = {}", rows);

    return rows;
  }
}
