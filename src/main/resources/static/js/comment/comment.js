import { ajax, PaginationUI } from '/js/common.js';


//<댓글기능 추가>
//1. DOMContentLoaded 시 댓글 목록 조회
document.addEventListener("DOMContentLoaded", () => {
  const boardId = document.getElementById("boardIdHidden").value;
  configCommentPagination(boardId);
});
//HTML 페이지가 다 불러와졌을 때 이 안의 코드 실행.
//loadComments(boardId) : 아래에 정의할 함수. 실제로 댓글 목록을 서버에서 불러오고 HTML안 .reply_list에 댓글들 뿌려줌

//2. 댓글 페이징해서 가져오는 함수 (configCommentPagination(boardId))
async function configCommentPagination(boardId, pageNo = 1) {
  const url = `/api/comments/totCnt?boardId=${boardId}`;
  const result = await ajax.get(url);  // 강사님 방식
  const totalRecords = result.body;

  const handlePageChange = (pageNo) => 
  loadComments(boardId, pageNo); // → 페이지 클릭 시 호출할 함수

  const pagination = new PaginationUI("reply_pagenation", handlePageChange);
  pagination.setTotalRecords(totalRecords);
  pagination.setRecordsPerPage(5);
  pagination.setPagesPerPage(5);
  pagination.handleFirstClick();  // 첫 페이지 자동 클릭
}



//2. 댓글 목록 조회 함수 정의(loadComments())
function loadComments(boardId, pageNo) {
  fetch(`/api/comments/paging?boardId=${boardId}&pageNo=${pageNo}&numOfRows=5`)
    .then((res) => res.json())
    .then((data) => {
      const listEl = document.querySelector(".reply_list");
      listEl.innerHTML = "";

      if (data.header.rtcd === "S00") {
        const comments = data.body;

        if (comments.length === 0) {
          listEl.textContent = "댓글이 없습니다.";
          return;
        }

        comments.forEach((comment) => {
          const div = document.createElement("div");
          div.classList.add("comment-item");
          div.innerHTML = `
            <p class="comment-writer"><strong>${comment.writer}</strong></p>
            <p class="comment-content" data-id="${comment.commentsId}">${comment.content}</p>
            <div class="comment-actions">
              <button class="btn-edit" data-id="${comment.commentsId}">수정</button>
              <button class="btn-delete" data-id="${comment.commentsId}">삭제</button>
            </div>
            <hr>
          `;
          listEl.appendChild(div);
        });
      } else {
        listEl.textContent = "댓글을 불러오지 못했습니다.";
      }
    });
}




//3. 댓글 등록 이벤트 핸들러
document
  .getElementById("commentForm")
  .addEventListener("submit", function (e) {
    e.preventDefault();

    const boardId = document.getElementById("boardIdHidden").value;
    const writer = document.getElementById("writer").value;
    const content = document.getElementById("content").value;

    fetch("/api/comments", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ boardId, writer, content }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.header.rtcd === "S00") {
          this.reset();
          configCommentPagination(boardId); // 페이징 포함해서 첫 페이지부터 다시 보여주기
        } else {
          document.getElementById("error_reply").textContent =
            "댓글 등록 실패!";
        }
      });
  });



//4. 댓글 수정
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("btn-edit")) {
    const commentId = e.target.dataset.id;
    const contentP = document.querySelector(
      `.comment-content[data-id="${commentId}"]`
    );
    const originalContent = contentP.textContent;

    // 입력창 + 저장/취소 버튼으로 바꾸기
    contentP.innerHTML = `
      <input type="text" class="edit-input" value="${originalContent}">
      <button class="btn-save" data-id="${commentId}">저장</button>
      <button class="btn-cancel" data-id="${commentId}">취소</button>
    `;
  }

  // 저장 버튼
  if (e.target.classList.contains("btn-save")) {
    const commentId = e.target.dataset.id;
    const input = document.querySelector(
      `.comment-content[data-id="${commentId}"] .edit-input`
    );
    const newContent = input.value;
    const boardId = document.getElementById("boardIdHidden").value;

    fetch(`/api/comments/${commentId}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content: newContent }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.header.rtcd === "S00") {
          loadComments(boardId, currentPage);
        }
      });
  }

  // 취소 버튼
  if (e.target.classList.contains("btn-cancel")) {
    const commentId = e.target.dataset.id;
    const contentP = document.querySelector(
      `.comment-content[data-id="${commentId}"]`
    );
    const boardId = document.getElementById("boardIdHidden").value;

    // 그냥 목록 다시 불러서 원래대로 되돌리기
    loadComments(boardId);
  }
});

//5. 댓글 삭제
// 댓글 삭제
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("btn-delete")) {
    const commentId = e.target.dataset.id;
    if (confirm("댓글을 삭제하시겠습니까?")) {
      fetch(`/api/comments/${commentId}`, {
        method: "DELETE",
      })
        .then((res) => res.json())
        .then((data) => {
          if (data.header.rtcd === "S00") {
            const boardId =
              document.getElementById("boardIdHidden").value;
            loadComments(boardId, currentPage);
          }
        });
    }
  }
});


