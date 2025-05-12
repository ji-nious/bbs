package com.kh.bbs.domain.board.svc;

import com.kh.bbs.domain.board.dao.BoardDAO;
import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardSVCImpl implements BoardSVC{

  private final BoardDAO boardDAO;

  //게시판 글 작성하고 등록 클릭했을 때 사용자가 적은 데이터를 DAO를 실행하게하는 로직
  @Override
  public Long save(Board board) { return boardDAO.save(board); }
}
