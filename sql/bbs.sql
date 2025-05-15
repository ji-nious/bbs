SELECT * FROM board;

-----------------
--게시판 테이블
-----------------
CREATE TABLE board (
  board_id    NUMBER PRIMARY KEY,                -- 게시글 ID
  title       VARCHAR2(200) NOT NULL,            -- 제목
  content     CLOB NOT NULL,                     -- 내용
  writer      VARCHAR2(50) NOT NULL,             -- 작성자 (email 같은 값)
  created_at  TIMESTAMP DEFAULT systimestamp,    -- 작성일
  updated_at  TIMESTAMP DEFAULT systimestamp     -- 수정일
);

-- 시퀀스
CREATE SEQUENCE board_board_id_seq;

------------------------------------------------------------------
--sql검증 생성, 조회, 수정, 삭제, 목록
--생성
INSERT INTO board (board_id, title, content, writer)
VALUES (board_board_id_seq.nextval, '첫 번째 글', '내용입니다.', 'test1@kh.com');
--조회
SELECT * 
  FROM board 
 WHERE board_id = 1;
--수정
UPDATE board 
   SET title = '수정된 제목', content = '수정된 내용'
 WHERE board_id = 1;
--삭제
DELETE FROM board WHERE board_id = 1;
--목록
  SELECT board_id, title, writer, created_at 
    FROM board 
ORDER BY created_at DESC;
------------------------------------------------------------------

COMMIT;