package com.kh.bbs.domain.board.dao;

import com.kh.bbs.domain.entity.Board;

public interface BoardDAO {
  //게시글 등록 - id기준이므로 데이터 타입은 Long
  Long save(Board board);

  //게시글 목록

  //게시글 상세

  //게시글 삭제

  //게시글 수정
}
