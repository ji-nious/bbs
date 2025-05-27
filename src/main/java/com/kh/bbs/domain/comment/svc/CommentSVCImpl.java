package com.kh.bbs.domain.comment.svc;

import com.kh.bbs.domain.comment.dao.CommentDAO;
import com.kh.bbs.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentSVCImpl implements CommentSVC{

  private final CommentDAO commentDAO;


  @Override
  public Long save(Comment comment) {
    return commentDAO.save(comment);
  }

  @Override
  public List<Comment> findAll(Long boardId) {
    return commentDAO.findAll(boardId);
  }

  @Override
  public List<Comment> findAll(Long boardId, int pageNo, int numOfRows) {
    return commentDAO.findAll(boardId, pageNo, numOfRows);
  }

  @Override
  public int getTotalCount(Long boardId) {
    return commentDAO.getTotalCount(boardId);
  }


  @Override
  public Optional<Comment> findById(Long id) {
    return commentDAO.findById(id);
  }

  @Override
  public int deleteById(Long id) {
    return commentDAO.deleteById(id);
  }

  @Override
  public int updateById(Long id, Comment comment) {
    return commentDAO.updateById(id, comment);
  }


}
