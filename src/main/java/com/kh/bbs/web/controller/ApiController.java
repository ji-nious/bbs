package com.kh.bbs.web.controller;

import com.kh.bbs.domain.comment.svc.CommentSVC;
import com.kh.bbs.domain.entity.Comment;
import com.kh.bbs.web.common.ApiResponse;
import com.kh.bbs.web.common.ApiResponseCode;
import com.kh.bbs.web.form.api.SaveApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/bbs")
@RestController
@RequiredArgsConstructor
public class ApiController {

  private final CommentSVC commentSVC;


  //댓글 생성     //      POST      /bbs  =>    POST http://localhost:9080/api/bbs
  //(Json -> 객체 -> DB)
  public ResponseEntity<ApiResponse<Comment>> add(
      @RequestBody @Valid SaveApi saveApi) {  //요청메시지 body에 포함된 json포멧 문자열을 java 객체로 변환
    Comment comment = new Comment();  //저장할 실제 엔티티 객체 생성
    BeanUtils.copyProperties(saveApi, comment);  //필드명 일치 시 자동 복사

    Long id = commentSVC.save(comment);  // service 통해 DB저장 후 생성된 ID 반환
    Comment findedComment = commentSVC.findById(id).orElseThrow(); // id로 DB 조회한 댓글 JSON으로 리턴

    //
    ApiResponse<Comment> response = ApiResponse.  of(ApiResponseCode.SUCCESS, findedComment);

    // HTTP 201 Created 상태코드와 함께 응답 객체 전달
    return ResponseEntity.status(HttpStatus.CREATED).body(findedComment);
  }

  //댓글 수정

  //댓글 삭제

  //댓글 목록




}
