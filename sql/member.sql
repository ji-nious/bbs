-- 1. 테이블, 시퀀스 삭제
DROP TABLE MEMBER;
DROP SEQUENCE member_member_id_seq;

-- 2. 테이블 생성
create table member (
    member_id   number(10),     --내부 관리 아이디
    email       varchar2(50),   --로긴 아이디
    passwd      varchar2(12),   --로긴 비밀번호
    nickname    varchar2(30),   --별칭
    gubun       varchar2(11)   default 'M0101', --회원구분 (일반,우수,관리자..)
    cdate       timestamp default systimestamp,         --생성일시
    udate       timestamp default systimestamp          --수정일시
);
-- 3. 기본키생성
alter table member add Constraint member_member_id_pk primary key (member_id);

-- 4. 외래키생성
alter table member add constraint member_gubun_fk
    foreign key(gubun) references code(code_id);

-- 5. 제약조건
alter table member modify email constraint member_email_uk unique;
alter table member modify email constraint member_email_nn not null;

-- 6. 시퀀스 생성
create sequence member_member_id_seq;

-- 7. 샘플데이터 of member
insert into member (member_id,email,passwd,nickname, gubun)
    values(member_member_id_seq.nextval, 'test1@kh.com', '1234', '테스터1', 'M0101');
insert into member (member_id,email,passwd,nickname, gubun)
    values(member_member_id_seq.nextval, 'test2@kh.com', '1234', '테스터2', 'M0102');
insert into member (member_id,email,passwd,nickname, gubun)
    values(member_member_id_seq.nextval, 'admin1@kh.com', '1234', '관리자1', 'M01A1');
insert into member (member_id,email,passwd,nickname, gubun)
    values(member_member_id_seq.nextval, 'admin2@kh.com', '1234', '관리자2', 'M01A2');

-- 8. email로 회원 찾기
SELECT count(*)
	FROM MEMBER
	WHERE email = 'admin2@kh.com';

select * from member;

commit;
