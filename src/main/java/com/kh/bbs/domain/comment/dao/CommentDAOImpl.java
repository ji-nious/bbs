package com.kh.bbs.domain.comment.dao;

import com.kh.bbs.domain.entity.Comment;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentDAOImpl implements CommentDAO {

  private final NamedParameterJdbcTemplate template;

  //수동매핑
  private RowMapper<Comment> doRowMapper(){
    //내부적으로 JDBC가 SQL 실행 -> ResultSet 획득,
    // doRowMapper()를 각 행마다 호출 -> rs, rowNum전달,
    // 각각 Comment 객체로 변환됨 -> List에 담김

    return(rs, rowNum)-> {
      Comment comment = new Comment();
      comment.setCommentsId(rs.getLong("comments_id"));
      comment.setBoardId((rs.getLong("board_id")));
      comment.setContent(rs.getString("content"));
      comment.setWriter(rs.getString("writer"));
      return comment;
    };
  }


  /**
   * 댓글 저장
   * @param comment
   * @return 댓글번호
   */
  @Override
  public Long save(Comment comment) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO comments(comments_id, board_id, content, writer) ");
    sql.append("VALUES(comments_comments_id_pk.nextval, :boardId, :content, :writer) ");
    //':변수명' = 객체.get필드명(db 컬럼명에 해당하는)

    SqlParameterSource param=  new BeanPropertySqlParameterSource(comment);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    long rows = template.update(sql.toString(), param, keyHolder, new String[]{"comments_id"});

    return ((Number)keyHolder.getKeys().get("comments_id")).longValue();
  }

  /**
   * 댓글목록
   * @return 댓글목록
   */
  @Override
  public List<Comment> findAll(Long boardId) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT comments_id, content, writer, created_at, updated_at ");
    sql.append("    FROM comments ");
    sql.append("   WHERE board_id = :boardId ");
    sql.append("ORDER BY COMMENTS_ID desc ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("boardId", boardId);

    return template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(Comment.class));
  }


  /**
   * 댓글목록 - 페이징
   * @param pageNo
   * @param numOfRows
   * @return
   */
  @Override
  public List<Comment> findAll(Long boardId, int pageNo, int numOfRows) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * FROM ( ");
    sql.append("  SELECT c.*, ROWNUM rn FROM ( ");
    sql.append("    SELECT * FROM comments ");
    sql.append("     WHERE board_id = :boardId ");
    sql.append("     ORDER BY comments_id DESC ");
    sql.append("  ) c ");
    sql.append("  WHERE ROWNUM <= (:pageNo * :numOfRows) ");
    sql.append(") ");
    sql.append("WHERE rn > ((:pageNo - 1) * :numOfRows) ");

    Map<String, Object> map = Map.of("boardId", boardId, "pageNo", pageNo, "numOfRows", numOfRows);
    List<Comment> list = template.query(sql.toString(), map, doRowMapper());

    return list;
  }

  @Override
  public int getTotalCount(Long boardId) { //board_id가 없는 경우 0반환. -> Optional 처리 불필요
    String sql = "SELECT COUNT(*) FROM comments WHERE board_id = :boardId ";

    Map<String, Object> param = Map.of("boardId", boardId);

    return template.queryForObject(sql, param, Integer.class);
  }
  /**
   * 댓글 삭제
   * @param id
   * @return 삭제된 행의 수
   */
  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM comments ");
    sql.append("WHERE comments_id = :id ");

    Map<String, Long> param = Map.of("id", id);
    int rows = template.update(sql.toString(), param);
    return rows;
  }


  /**
   * 댓글 수정
   * @param id
   * @param comment
   * @return 수정된 행의 수
   */
  @Override
  public int updateById(Long id, Comment comment) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE comments ");
    sql.append("SET content = :content, writer = :writer, ");
    sql.append("    updated_at = systimestamp ");
    sql.append("WHERE comments_id = :commentsId ");

    //수동매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("content", comment.getContent())
        .addValue("writer", comment.getWriter())
        .addValue("commentsId", id);

    int rows = template.update(sql.toString(), param);

    return rows;
  }


  /**
   * 댓글 조회
   * @param id
   * @return 댓글객체
   */
  @Override
  public Optional<Comment> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COMMENTs_id, board_id, content, writer, created_at, updated_at ");
    sql.append("FROM comments ");
    sql.append("WHERE comments_id = :commentsId ");


    SqlParameterSource param = new MapSqlParameterSource().addValue("commentsId", id);

    Comment comment = null;
    try {
      comment = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(Comment.class));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }

    return Optional.of(comment);
  }
}
