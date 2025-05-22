package com.kh.bbs.domain.comment.dao;

import com.kh.bbs.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDAO {
  //댓글 등록 - id기준이므로 데이터 타입은 Long
  Long save(Comment comment);

  //댓글 목록
  List<Comment> findAll();

  //댓글 삭제
  int deleteById(Long id);

  //댓글 수정
  int updateById(Long Id, Comment comment);

  //댓글 찾기
  Optional<Comment> findById(Long id);
}
