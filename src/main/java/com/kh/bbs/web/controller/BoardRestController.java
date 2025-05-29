package com.kh.bbs.web.controller;

import com.kh.bbs.domain.board.svc.BoardSVC;
import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.web.common.ApiResponse;
import com.kh.bbs.web.common.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardRestController {

  private final BoardSVC boardSVC;

  @GetMapping("/paging")
  public ResponseEntity<ApiResponse<List<Board>>> list(
      @RequestParam(value = "numOfRows", defaultValue = "10") Integer numOfRows,
      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo
  ) {
    List<Board> boards = boardSVC.findAll(pageNo, numOfRows);
    int totalCount = boardSVC.totalCount();

    ApiResponse<List<Board>> res = ApiResponse.of(
        ApiResponseCode.SUCCESS,
        boards,
        new ApiResponse.Paging(pageNo, numOfRows, totalCount)
    );

    return ResponseEntity.ok(res);
  }
}
