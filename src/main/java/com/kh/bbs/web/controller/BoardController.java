package com.kh.bbs.web.controller;

import com.kh.bbs.domain.board.svc.BoardSVC;
import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.web.form.bbs.DetailForm;
import com.kh.bbs.web.form.bbs.SaveForm;
import com.kh.bbs.web.form.bbs.UpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/boards")
@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

  // 데이터베이스와 연결되어 로직을 수행하는 친구 선언!
  // 얘가 수행해서 얻은 결과값을 컨트롤러가 view단에 연결해주는 거임
  private final BoardSVC boardSVC;

  //목록
  @GetMapping       // GET  http://localhost:9080/products
  public String BoardList(Model model) {
    List<Board> list = boardSVC.findAll();
    model.addAttribute("list", list);
    return "board/all";   //view(resources/templates/board/all)
  }

  //'글쓰기'등록 화면
  //클라이언트가 '글쓰기' 버튼을 누르면 실행될 액션을 정의해주는 거임
  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("saveForm", new SaveForm());
    return "board/add"; //templates/board/add.html
  }

  //'글쓰기'등록 처리, '저장'버튼
  @PostMapping("/add")
  public String add(SaveForm saveForm, RedirectAttributes redirectAttributes) {
    log.info("writer = {}, title = {}, content = {}", saveForm.getWriter(), saveForm.getTitle(), saveForm.getContent());

    Board board = new Board();
    board.setWriter(saveForm.getWriter());
    board.setTitle(saveForm.getTitle());
    board.setContent(saveForm.getContent());
    board.setCreatedAt(LocalDateTime.now());
    board.setUpdatedAt(LocalDateTime.now());

    Long bid = boardSVC.save(board);

    log.info("생성된 게시글 ID: {}", bid);

    redirectAttributes.addAttribute("id", bid);

    return "redirect:/boards/{id}";
  }

  //게시글조회
  @GetMapping("/{id}")      // GET http://localhost:9080/products/2?name=홍길동&age=20
  public String findById(
      @PathVariable("id") Long id,        // 경로변수 값을 읽어올때
      Model model
//      @RequestParam("name") String name,  // 쿼리파라미터 값을 읽어올때
//      @RequestParam("age") Long age
  ){

    log.info("id={}",id);
//    log.info("name={}",name);
//    log.info("age={}",age);

    Optional<Board> optionalBoard = boardSVC.findById(id);
    Board findedBoard = optionalBoard.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setBoardId((findedBoard.getBoardId()));
    detailForm.setTitle(findedBoard.getTitle());
    detailForm.setWriter(findedBoard.getWriter());
    detailForm.setContent(findedBoard.getContent());
    detailForm.setCreatedAt(findedBoard.getCreatedAt());
    detailForm.setUpdatedAt(findedBoard.getUpdatedAt());


    log.info("DetailForm 개체: {}", detailForm);

    model.addAttribute("detailForm",detailForm);

    return "board/detailForm";   //상품상세화면
  }


  //게시글 삭제
  @GetMapping("/{id}/del")
  public String deleteById (
    //@RequestParm("id") Long productId
    @PathVariable("id") Long productId) {

      int rows = boardSVC.deleteById(productId);

      return "redirect:/boards";      // 302 get redirectUrl: http://localhost:9080/products

  }


  //게시글수정화면
  @GetMapping("/{id}/edit")         // GET http://localhost:9080/2/edit
  public String updateForm(
      @PathVariable("id") Long boardId,
      Model model
  ) {
    //1) 유효성체크
    //2) 기존 게시글 조회하고 내용 담아서 수정페이지로 이동시킴
    Optional<Board> optionalBoard = boardSVC.findById(boardId);
    Board findedBoard = optionalBoard.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setBoardId(findedBoard.getBoardId());
    updateForm.setTitle(findedBoard.getTitle());
    updateForm.setWriter(findedBoard.getWriter());
    updateForm.setContent(findedBoard.getContent());
    updateForm.setCreatedAt(findedBoard.getCreatedAt());
    updateForm.setUpdatedAt(findedBoard.getUpdatedAt());

    log.info("updateForm={}",updateForm);

    model.addAttribute("updateForm",updateForm);
    return "board/updateForm_new";
  }

  //상품수정처리
  @PostMapping("/{id}/edit")         // POST http://localhost:9080/2/edit
  public String updateById(
      @PathVariable("id") Long boardId,
      UpdateForm updateForm,
      RedirectAttributes redirectAttributes
  ){
    log.info("id={}", boardId);
    log.info("updateForm={}",updateForm);

    Board board = new Board();
    board.setBoardId(updateForm.getBoardId());
    board.setTitle(updateForm.getTitle());
    board.setWriter(updateForm.getWriter());
    board.setContent(updateForm.getContent());
    board.setCreatedAt(updateForm.getCreatedAt());
    board.setUpdatedAt(LocalDateTime.now());

    int rows = boardSVC.updateById(boardId, board);

    redirectAttributes.addAttribute("id",boardId);
    return "redirect:/boards/{id}";  // 302 get redirectUrl-> http://localhost/products/id
  }


}
