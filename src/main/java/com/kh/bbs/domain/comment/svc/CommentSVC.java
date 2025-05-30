package com.kh.bbs.domain.comment.svc;

import com.kh.bbs.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentSVC {
  Long save(Comment comment);

  List<Comment> findAll(Long boardId);

  List<Comment> findAll(Long boardId, int pageNo, int numOfRows);

  int getTotalCount(Long boardId);

  Optional<Comment> findById(Long id);

  int deleteById(Long id);

  int updateById(Long id, Comment comment);


}
