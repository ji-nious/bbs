package com.kh.bbs.domain.comment.dao;

import com.kh.bbs.domain.entity.Comment;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class CommentDAOImplTest {

  @Autowired
  CommentDAO commentDAO;


  @Test
  @DisplayName("댓글등록")
  void save() {
    //given
    Comment comment = new Comment();
    comment.setContent("우왕 굳");
    comment.setWriter("이용자1");

    //when
    Long commentId = commentDAO.save(comment);

    //then
    Assertions.assertThat(commentId).isNotNull();
    Assertions.assertThat(commentId).isGreaterThan(0);
  }

  @Test
  @DisplayName("댓글 목록")
  void findAll() {
    //given
    Comment comment1 = new Comment();
    comment1.setContent("테스트 내용1");
    comment1.setWriter("테스트 작성자1");

    Comment comment2 = new Comment();
    comment2.setContent("테스트 내용2");
    comment2.setWriter("테스트 작성자2");

    commentDAO.save(comment1);
    commentDAO.save(comment2);

    //when
    List<Comment> commentList = commentDAO.findAll();

    //then
    Assertions.assertThat(commentList).isNotNull();
    Assertions.assertThat(commentList.size()).isEqualTo(6);
  }


  @Test
  @DisplayName("댓글찾기")
  void findById() {
    //given
    Comment comment = new Comment();
    comment.setContent("테스트 내용");
    comment.setWriter("테스트 작성자");
    Long commentsId = commentDAO.save(comment);

    //when
    Optional<Comment> foundComment = commentDAO.findById(commentsId);

    //then
    Assertions.assertThat(foundComment).isPresent();
    Assertions.assertThat(foundComment.get().getContent()).isEqualTo("테스트 내용");
    Assertions.assertThat(foundComment.get().getWriter()).isEqualTo("테스트 작성자");
  }


  @Test
  void deleteById() {
    //given
    Comment comment = new Comment();
    comment.setContent("테스트 내용");
    comment.setWriter("테스트 작성자");
    Long commentsId = commentDAO.save(comment);

    //when
    int deletedRow = commentDAO.deleteById(commentsId);

    //then
    Assertions.assertThat(deletedRow).isEqualTo(1);
    Assertions.assertThat(commentDAO.findById(commentsId)).isEmpty();
  }

  @Test
  @DisplayName("댓글 수정")
  void updateById() {
    //given
    Comment comment = new Comment();
    comment.setContent("테스트 내용");
    comment.setWriter("테스트 작성자");
    Long commentsId = commentDAO.save(comment);

    Comment updatedComment = new Comment();
    updatedComment.setContent("테스트 내용_수정");
    updatedComment.setWriter("테스트 작성자_수정");


    //when
    int updatedRow = commentDAO.updateById(commentsId, updatedComment);
    Optional<Comment> foundComment = commentDAO.findById(commentsId);

    //then
    log.info("update 요청: id={}, content={}, writer={}",
        foundComment.get().getCommentsId(), foundComment.get().getContent(), foundComment.get().getWriter());
    Assertions.assertThat(updatedRow).isEqualTo(1);
    Assertions.assertThat(foundComment.get().getContent()).isEqualTo("테스트 내용_수정");
    Assertions.assertThat(foundComment.get().getWriter()).isEqualTo("테스트 작성자_수정");
  }
}