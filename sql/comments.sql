-- 1. 테이블 삭제
DROP TABLE comments;

-- 2. 테이블 생성 (board_id 추가)
CREATE TABLE comments (
  comments_id   NUMBER(10),        -- 댓글 ID (PK)
  board_id      NUMBER(10),        -- 게시글 ID (FK)
  content       VARCHAR2(3000),    -- 댓글 내용
  writer        VARCHAR2(30),      -- 작성자
  created_at    TIMESTAMP DEFAULT systimestamp, -- 작성일시
  updated_at    TIMESTAMP DEFAULT systimestamp  -- 수정일시
);

-- 3. 기본키 제약조건 추가
ALTER TABLE comments ADD CONSTRAINT comments_comments_id_pk PRIMARY KEY (comments_id);

-- 4. 외래키 제약조건 추가 (board 테이블이 존재한다고 가정)
ALTER TABLE comments ADD CONSTRAINT comments_board_id_fk FOREIGN KEY (board_id)
REFERENCES board(board_id);

-- 5. 시퀀스 삭제
DROP SEQUENCE comments_comments_id_seq;

-- 6. 시퀀스 생성
CREATE SEQUENCE comments_comments_id_seq;

-- 7. 댓글 등록 예시
INSERT INTO comments(comments_id, board_id, content, writer)
VALUES (comments_comments_id_seq.NEXTVAL, 1, '댓글 내용1', '작성자1');

-- 8. 댓글 수정 예시
UPDATE comments
   SET content = '수정된 댓글 내용', writer = '수정자', updated_at = systimestamp
 WHERE comments_id = 1;

-- 9. 댓글 삭제 예시
DELETE FROM comments WHERE comments_id = 1;

-- 10. 댓글 목록 (게시글별)
SELECT comments_id, board_id, content, writer, created_at, updated_at
  FROM comments
 WHERE board_id = :boardId
 ORDER BY comments_id DESC;

-- 11. 댓글 목록 - 페이징 (게시글 기준)
SELECT *
  FROM (
    SELECT c.*, ROWNUM rn
      FROM (
        SELECT * FROM comments
         WHERE board_id = :boardId
         ORDER BY comments_id DESC
      ) c
     WHERE ROWNUM <= (:pageNo * :numOfRows)
  )
 WHERE rn > ((:pageNo - 1) * :numOfRows);

-- 12. 댓글 총 개수 (게시글 기준)
SELECT COUNT(*) FROM comments WHERE board_id = :boardId;

-- 13. 댓글 상세 조회
SELECT * FROM comments WHERE comments_id = :id;

-- 14. 전체 목록 보기 (관리자 용도 등)
SELECT * FROM comments ORDER BY comments_id DESC;

-- 15. 롤백 / 커밋
ROLLBACK;
COMMIT;