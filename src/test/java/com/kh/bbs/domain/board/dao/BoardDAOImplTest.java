package com.kh.bbs.domain.board.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest  //Spring Boot 애플리케이션 전체 로딩해서 실제로 DB와 연결되어 있는 환경에서 테스트
class BoardDAOImplTest {

  @Autowired //boardDAO 테스트 할거니까 해당 객체 주입
  BoardDAO boardDAO;

  @Test
  void save() {

  }
}