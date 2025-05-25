package com.kh.bbs.domain.comment.dao;

import com.kh.bbs.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDAO {
  //댓글 등록 - id기준이므로 데이터 타입은 Long
  Long save(Comment comment);

  //댓글 목록
  List<Comment> findAll(Long boardId);

  //댓글 목록 - 페이징 (특정 게시글 기준)
  List<Comment> findAll(Long boardId, int pageNo, int numOfRows);

  //댓글 총 건수 (특정 게시글 기준)
  int getTotalCount(Long boardId);

  //댓글 삭제
  int deleteById(Long id);

  //댓글 수정
  int updateById(Long Id, Comment comment);

  //댓글 상세
  Optional<Comment> findById(Long id);


}
