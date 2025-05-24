package com.kh.bbs.web.controller;

import com.kh.bbs.domain.comment.svc.CommentSVC;
import com.kh.bbs.domain.entity.Comment;
import com.kh.bbs.web.common.ApiResponse;
import com.kh.bbs.web.common.ApiResponseCode;
import com.kh.bbs.web.form.api.SaveApi;
import com.kh.bbs.web.form.api.UpdateApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequestMapping("/api/comments")
@RestController  //리턴 객체를 자동으로 JSON으로 변환시켜줌 [@Controller + @Responsebody(내부적으로 JSON 변환 수행)]
@RequiredArgsConstructor
public class CommentController {

  private final CommentSVC commentSVC;


  //댓글 생성     //      POST      /comments => POST http://localhost:9090/api/comments
  //(Json -> 객체 -> DB)
  @PostMapping
  public ResponseEntity<ApiResponse<Comment>> add(  //ResponseEntity<T> Spring에서 응답(Response)를 만들 때 사용하는 '응답용 래퍼 클래스'
      @RequestBody @Valid SaveApi saveApi) {  //요청메시지 body에 포함된 json포멧 문자열을 java 객체로 변환
    Comment comment = new Comment();  //저장할 실제 엔티티 객체 생성
    BeanUtils.copyProperties(saveApi, comment);  //필드명 일치 시 자동 복사

    Long id = commentSVC.save(comment);  // service 통해 DB저장 후 생성된 ID 반환
    Comment findedComment = commentSVC.findById(id).orElseThrow(); // id로 DB 조회한 댓글 JSON으로 리턴

    //ApiResponse<Comment> : Comment를 담을 응답 포맷
    ApiResponse<Comment> commentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedComment);

    // HTTP 201 Created 상태코드와 함께 응답 객체 전달
    // "응답 코드 201로 응답을 보내되,
    // 응답 본문에는 commentApiResponse 객체(JSON 형태)가 담기도록 해줘" 라는 뜻
    return ResponseEntity
        .status(HttpStatus.CREATED)   // 1. 상태 코드 설정 (201 created)
        .body(commentApiResponse);    // 2. 응답 본문(body)에 객체 담기
  }


  //댓글 수정     // PATCH      /comments/{id} => PATCH http://localhost:9090/api/comments/{id}
  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<Comment>> updateById(
      @PathVariable("id") Long id,
      @RequestBody UpdateApi updatedApi
  ) {

    //1) 댓글 조회
    Optional<Comment> optionalComment = commentSVC.findById(id);
    Comment findedComment = optionalComment.orElseThrow(    // 찾는 댓글이 있으면 값return, 없으면 예외 발생.
        ()->new NoSuchElementException("댓글번호 : " + id + "를 찾을 수 없습니다.") //람다식. "optionalComment가 비어있으면 -> 이 람다를 실행해서 예외를 던져줘!"
    );  // 찾고자 하는 댓글이 없으면 NoSuchElementException 예외발생

    //2) 댓글 수정
    Comment comment = new Comment();
//    BeanUtils.copyProperties(updateApi, comment);  //updatedApi부분이 전체 덮어쓰기 되어버림. 즉, writer : null됨!

    //부분 수정 - 수동으로 매핑하기
    comment.setContent(updatedApi.getContent());
    comment.setWriter(findedComment.getWriter());  // 기존 값 유지

    commentSVC.updateById(id, comment);

    log.info("변경할 content = {}", updatedApi.getContent());
    log.info("기존 writer = {}", findedComment.getWriter());


    //3) 수정된 댓글 조회
    optionalComment = commentSVC.findById(id);
    Comment updatedComment = optionalComment.orElseThrow();

    //4) REST API 응답 표준 메시지 생성
    ApiResponse<Comment> commentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, updatedComment); //위 로직 다 실행하고 '성공했다'는 메세지를 클라이언트에 전달해야함. 그래서 SUCCESS 쓰는거임. 만약 오류가 났다면 이를 처리하는 예외 처리 기능을 만들어 주면 됨.

    //5) HTTP 응답 메세지 생성
    return ResponseEntity.ok(commentApiResponse);
  }


  //댓글 삭제     //DELETE    /comments/{id} => DELETE http://localhost:9090/api/comments/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Comment>> deleteById(@PathVariable("id") Long id) {
    //1) 댓글 조회
    Optional<Comment> optionalComment = commentSVC.findById(id);
    Comment findedComment = optionalComment.orElseThrow(    // 찾는 댓글이 있으면 값return, 없으면 예외 발생.
        ()->new NoSuchElementException("댓글번호 : " + id + "를 찾을 수 없습니다.") //람다식. "optionalComment가 비어있으면 -> 이 람다를 실행해서 예외를 던져줘!"
    );  // 찾고자 하는 댓글이 없으면 NoSuchElementException 예외발생

    //2) 댓글 삭제
    commentSVC.deleteById(id);

    //3) REST API 표준 응답 생성
    ApiResponse<Comment> commentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedComment);

    //4) HTTP응답 메세지 생성
    return ResponseEntity.ok(commentApiResponse);
  }


  //댓글 목록     //GET     /comments => GET http://localhost:9090/api/comments
  @GetMapping
  public ResponseEntity<ApiResponse<List<Comment>>> findAll(
      @RequestParam("boardId") Long boardId
  ) {

    List<Comment> list = commentSVC.findAll(boardId);
    ApiResponse<List<Comment>> listApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, list);

    return ResponseEntity.ok(listApiResponse);
  }


//  //댓글 목록-페이징     //GET     /comments => GET http://localhost:9090/api/comments/paging
//  @GetMapping("/paging")
//  public ResponseEntity<ApiResponse<List<Comment>>> findAll(
//      //@RequestParam : HTTP 요청의 쿼리 파라미터값을 메서드의 매개변수로 바인딩
//      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
//      @RequestParam(value = "numOfRows", defaultValue = "10") Integer numOfRows
//  ) {
//    log.info("pageNo={}, numOfRows={}", pageNo, numOfRows);
//
//    //상품목록 가져오기
//    List<Comment> list = commentSVC.findAll(pageNo, numOfRows);
//
//    //상품 총 건수 가져오기
//    int totalCount = commentSVC.getTotalCount();
//
//    //REST API 표준 응답 만들기
//    ApiResponse<List<Comment>> listApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS,
//        list,
//        new ApiResponse.Paging(pageNo, numOfRows, totalCount)
//    );
//    return ResponseEntity.ok(listApiResponse);
//  }
//
//  //전체 건수 가져오기      //GET     /comments/totCnt => GET http://localhost:9090/api/comments/totCnt
//  @GetMapping("/totCnt")
//  public ResponseEntity<ApiResponse<Integer>> totalCount() {
//
//    int totalCount = commentSVC.getTotalCount();
//    ApiResponse<Integer> commentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, totalCount);
//
//    return ResponseEntity.ok(commentApiResponse); //상태코드 200, 응답메세지 Body:commentApiResponse객체가 json포맷 문자열로 변환됨
//  }





}
