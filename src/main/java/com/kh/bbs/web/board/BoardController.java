package com.kh.bbs.web.board;

import com.kh.bbs.domain.board.svc.BoardSVC;
import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.web.form.SaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

  // 데이터베이스와 연결되어 로직을 수행하는 친구 선언!
  // 얘가 수행해서 얻은 결과값을 컨트롤러가 view단에 연결해주는 거임
  private final BoardSVC boardSVC;


  //'글쓰기'버튼
  //클라이언트가 '글쓰기' 버튼을 누르면 실행될 액션을 정의해주는 거임
  @GetMapping("/add")
  public String addForm() {
    return "product/add";
  }

  //'저장'버튼
  @PostMapping("/add")
  public String add(SaveForm saveForm, RedirectAttributes redirectAttributes) {
    log.info("bwriter = {}, btitle = {}, bcontent = {}", saveForm.getBwriter(), saveForm.getBtitle(), saveForm.getBcontent());

    Board board = new Board();
    board.setWriter(saveForm.getBwriter());
    board.setTitle(saveForm.getBtitle());
    board.setContent(saveForm.getBcontent());
    board.getCreatedAt();
    board.getUpdatedAt();

    Long bid = boardSVC.save(board);

    return "redirect:/products/{id}";
  }

}
