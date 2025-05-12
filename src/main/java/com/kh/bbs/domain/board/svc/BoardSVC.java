package com.kh.bbs.domain.board.svc;

import com.kh.bbs.domain.entity.Board;

public interface BoardSVC {
  // 게시판 글쓰기
  Long save(Board board);

}
