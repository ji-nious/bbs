package com.kh.bbs.domain.board.dao;

import com.kh.bbs.domain.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest  //Spring Boot 애플리케이션 전체 로딩해서 실제로 DB와 연결되어 있는 환경에서 테스트
class BoardDAOImplTest {

  @Autowired //boardDAO 테스트 할거니까 해당 객체 주입
  BoardDAO boardDAO;

  @Test
  @DisplayName("게시판 글쓰기")
  void save() {
    //1. 일단 내가 게시판에서 게시글을 적는다 가정하고 코드작성. = Board 객체 필드에 내용 추가(DB 행 만들기)
    Board board = new Board();
    board.setWriter("허진희");
    board.setTitle("수변공원 회센타에서 피해야 하는 가게들 좀 알려주실 수 있나요?");
    board.setContent("다들 맛집만 있어서 그런데... 피해야 하는 가게부터 알아야 할거 같아요.. 다들 경험 공유좀..");
    board.getCreatedAt();
    board.getUpdatedAt();

    //2. 그다음에 내가 만든 Board객체(DB행에 해당)를 실제로 DB에 넣어줌.
    //   이게 NamedParameterJdbcTemplate template를 사용해서 BoardDAOImple클래스에 만들어둔 save메서드.
    //   따라서 아래와 같이 만들어둔 save 메서드 호출해서 실행해봄.
    long rows = boardDAO.save(board);

    log.info("상품번호 = {}", rows);
  }


  @Test
  @DisplayName("목록조회")
  void findAll() {
    List<Board> list = boardDAO.findAll();

//    log.info("상품목록={}", list);
    for (Board board : list) {
      log.info("product={}", board);
    }
  }

  @Test
  @DisplayName("게시글조회")
  void findById() {
    Long boardId = 18L;
    Optional<Board> optionalBoard =  boardDAO.findById(boardId);
    Board findedBoard = optionalBoard.orElseThrow();

    log.info("findedBoard = {}", findedBoard);


  }


}