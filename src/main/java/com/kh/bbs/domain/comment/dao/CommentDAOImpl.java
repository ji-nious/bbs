package com.kh.bbs.domain.comment.dao;

import com.kh.bbs.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

  /**
   * 댓글 저장
   * @param comment
   * @return 댓글번호
   */
  @Override
  public Long save(Comment comment) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO comments(comments_id, content, writer) ");
    sql.append("VALUES(comments_comments_id_pk.nextval, :content, :writer) ");
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
  public List<Comment> findAll() {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT comments_id, content, writer, created_at, updated_at ");
    sql.append("FROM comments ");
    sql.append("ORDER BY COMMENTS_ID desc ");

    List<Comment> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Comment.class));

    return list;
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

  @Override
  public Optional<Comment> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COMMENTs_id, content, writer, created_at, updated_at ");
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
