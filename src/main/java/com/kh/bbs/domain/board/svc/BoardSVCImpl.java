package com.kh.bbs.domain.board.svc;

import com.kh.bbs.domain.board.dao.BoardDAO;
import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardSVCImpl implements BoardSVC{

  private final BoardDAO boardDAO;

  //상품목록 확인
  @Override
  public List<Board> findAll() { return boardDAO.findAll(); }

  //게시판 글 작성하고 등록 클릭했을 때 사용자가 적은 데이터를 DAO를 실행하게하는 로직
  @Override
  public Long save(Board board) { return boardDAO.save(board); }

  //게시글 조회
  @Override
  public Optional<Board> findById(Long id) { return boardDAO.findById(id); }

  @Override
  public int deleteById(Long id) { return boardDAO.deleteById(id); }


}

