package com.kh.bbs.domain.board.svc;

import com.kh.bbs.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardSVC {
  // 게시판 글쓰기
  Long save(Board board);

  // 게시판 목록
  List<Board> findAll();

  // 게시글 조회
  Optional<Board> findById(Long id);
}
