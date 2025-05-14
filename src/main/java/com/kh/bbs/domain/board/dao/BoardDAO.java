package com.kh.bbs.domain.board.dao;

import com.kh.bbs.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardDAO {
  //게시글 등록 - id기준이므로 데이터 타입은 Long
  Long save(Board board);

  //게시글 목록 - board 객체들을 목록으로 받을 거니까 데이터 타입은 List<Board>
  List<Board> findAll();

  //게시글 상세
  public Optional<Board> findById(Long id);
  //게시글 삭제

  //게시글 수정
}
