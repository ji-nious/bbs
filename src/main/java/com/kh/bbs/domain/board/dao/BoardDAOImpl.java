package com.kh.bbs.domain.board.dao;

import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j  //로깅하기 위한 어노테이션
@RequiredArgsConstructor
//생성자 생성. BoardDAOImpl 클래스는 데이터베이스와 상호작용하기 위해서 NamedParameterJdbcTemplate 같은 객체를 주입받아야 한다.
//이 객채를 new로 새로 생성하기보다 자동으로 생성자를 생성할 때에 주입 받는다.
//final 필드로 template를 지정하게 되므로 requiredArgsconstructor를 사용한다.
@Repository //DAO 클래스임을 명시
public class BoardDAOImpl implements BoardDAO{

  final NamedParameterJdbcTemplate template; //Entity Board 객체 필드와 SQL 내 파라미터 연결도구


  //수동매핑
  RowMapper<Board> boardRowMapper(){

    return (rs, rowNum)->{
      Board board = new Board();
      board.setBoardId(rs.getLong("board_id"));
      board.setTitle(rs.getString("title"));
      board.setWriter(rs.getString("writer"));

      // created_at 컬럼 매핑
      Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
      if (createdAtTimestamp != null) {
        board.setCreatedAt(createdAtTimestamp.toLocalDateTime());
      }

      // updated_at 컬럼 매핑
      Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
      if (updatedAtTimestamp != null) {
        board.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
      }

      return board;
    };
  }

  /**
   * 게시판 글쓰기(등록)
   * @param board
   * @return
   */
  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO board (board_id, title, content, writer, created_at, updated_at) ");
    sql.append("     VALUES (board_board_id_seq.nextval, :title , :content, :writer, :createdAt, :updatedAt ) ");

    //자바객체 필드명을 자동으로 파라미터명으로 인식. 해당 값을 SQL 쿼리의 파라미터에 자동으로 바인딩. Map 형태로 저장.
    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

    //INSERT, UPDATE, DELETE 쿼리에서 영향받은 행의 수(int)를 반환
//    long rows = template.update(sql.toString(), param);
//    log.info("삽입된 행의 수 = {}", rows);

    KeyHolder keyHolder = new GeneratedKeyHolder();

    long rows = template.update(sql.toString(), param, keyHolder, new String[]{"board_id"});
//    log.info("rows={}", rows);

    Number pidNumber = (Number)keyHolder.getKeys().get("board_id");
    long pid = pidNumber.longValue();

    return pid;
  }

  /**
   * 게시글 목록
   * @return 게시글 목록
   */
  @Override
  public List<Board> findAll() {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT BOARD_ID, TITLE, writer, CREATED_AT, UPDATED_AT ");
    sql.append("    FROM BOARD ");
    sql.append("ORDER BY BOARD_ID DESC ");

    //db요청
    List<Board> list = template.query(sql.toString(), boardRowMapper());

    return list;
  }

  /**
   * 게시글조회
   * @param id 게시글번호
   * @return 게시글정보
   */
  @Override
  public Optional<Board> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT BOARD_ID, TITLE, writer, CONTENT, CREATED_AT, UPDATED_AT ");
    sql.append("    FROM BOARD ");
    sql.append("   WHERE BOARD_ID = :id ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);

    Board board = null;
    try {
      board = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(Board.class));
    } catch (EmptyResultDataAccessException e) { //template.queryForObject() : 레코드를 못찾으면 예외 발생
      return Optional.empty();
    }

    return Optional.of(board);
  }

  /**
   * 게시글삭제(단건)
   * @param id 게시글번호
   * @return 삭제건수
   */
  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM board ");
    sql.append(" WHERE board_id = :id ");

    //case1)
//    SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);
    //case2)
    Map<String, Long> param = Map.of("id",id);
    int rows = template.update(sql.toString(), param); //삭제된 행의 수 반환
    return rows;
  }

  /**
   * 게시글 수정
   * @param boardId 게시글번호
   * @param board 게시글정보
   * @return 게시글 수정 건수
   */
  @Override
  public int updateById(Long boardId, Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE board ");
    sql.append("SET title = :title, content = :content, updated_at = :updatedAt ");
    sql.append("WHERE board_id = :boardId ");

    //수동매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("updatedAt", board.getUpdatedAt())
        .addValue("boardId", boardId);

    int rows = template.update(sql.toString(), param); // 수정된 행의 수 반환

    return rows;
  }

  /**
   * 게시글 목록 페이징
   * @param pageNo
   * @param numOfRows
   * @return 페이징 리스트
   */
  @Override
  public List<Board> findAll(int pageNo, int numOfRows) {
    int offset = (pageNo - 1) * numOfRows;

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT board_id,title,writer,created_at, updated_at ");
    sql.append("FROM board ");
    sql.append("ORDER BY board_id desc ");
    sql.append("OFFSET :offset ROWS ");
    sql.append("FETCH NEXT :limit ROWS only ");

    Map<String, Object> param = Map.of("offset", offset, "limit", numOfRows);

    List<Board> list = template.query(sql.toString(), param, boardRowMapper());

    return list;
  }

  @Override
  public int totalCount() {
    String sql = "SELECT COUNT(*) FROM board";
    return template.queryForObject(sql, Map.of(), Integer.class);
  }

}
