-- 테이블 삭제
DROP TABLE COMMENTS ;

-- 테이블 생성
CREATE TABLE comments(
	comments_id			number(10),				--게시글 식별자
	content				varchar2(3000),	  --댓글내용(한글 1000자)
	writer				varchar2(30),			--작성자(한글10자)
	created_at		timestamp,				--작성일시
	updated_at		timestamp					--수정일시
);

-- 기본키
ALTER TABLE comments ADD constraint comments_comments_id_pk PRIMARY key(comments_id);

-- 기본값
ALTER TABLE comments MODIFY created_at DEFAULT systimestamp;
ALTER TABLE comments MODIFY updated_at DEFAULT systimestamp;

-- 시퀀스 삭제
DROP SEQUENCE comments_comments_id_pk;

-- 시퀀스 생성
CREATE SEQUENCE comments_comments_id_pk;

SELECT * FROM comments;

-- 댓글 등록
INSERT INTO comments(comments_id, content, writer)
				VALUES(comments_comments_id_pk.nextval, '댓글내용1', '작성자1');

-- 댓글 수정
UPDATE comments
	 SET content = '수정댓글내용1', writer = '수정작성자',
	 		 updated_at = systimestamp
 WHERE comments_id = 1;

-- 댓글 삭제
DELETE FROM comments
 WHERE comments_id = 1;

-- 댓글 목록
SELECT comments_id, content, writer, created_at, updated_at
	FROM comments
ORDER BY COMMENTS_ID desc;

-- 댓글 목록 - 페이징
SELECT product_id,pname,quantity,price
FROM product
 ORDER BY product_id asc
OFFSET (:pageNo -1) * :numOfRows ROWS
FETCH NEXT :numOfRows ROWS only ;

-- 댓글 총 갯수
SELECT count(comments_id) FROM comments;

-- 댓글 찾기
SELECT COMMENTs_id, content, writer, created_at, updated_at
  FROM comments
 WHERE comments_id = 1;

ROLLBACK;

COMMIT;

SELECT * FROM COMMENTs ORDER BY comments_id desc;




