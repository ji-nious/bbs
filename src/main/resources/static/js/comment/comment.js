//댓글 목록
document.addEventListener("DOMContentLoaded", () => {
    const boardId = document.getElementById("boardIdHidden").value;
    loadPagedComments(boardId, 1)
});

async function loadPagedComments(boardId, pageNo=1, numOfRows=10) {
    try {
        const res = await fetch(`/api/comments/paging?boardId=${boardId}&pageNo=${pageNo}&numOfRows=${numOfRows}`);
        const result = await res.json();
        const comments = result.body || [];
        const paging = result.paging;

        const $list = document.getElementById("reply_list");
        $list.innerHTML = ""; // 기존 댓글 초기화

        comments.forEach(comment => {
            console.log(comment.writer, comment.content);
            const commentBox = document.createElement("div");
            commentBox.className = "comment-item";
            commentBox.innerHTML = `
                <div class="reply_list" id="reply_list">
                    <div class="comment-writer">${comment.writer}</div>
                    <div class="comment-content">${comment.content}</div>
                    <div class="btn-comment-group">
                        <button class="btn delete-btn" data-id="${comment.commentId}">삭제</button>
                        <button class="btn edit-btn" data-id="${comment.commentId}">수정</button>
                    </div>
                    <hr>
                </div>
            `
            $list.appendChild(commentBox);


            //댓글 삭제 기능 추가
             const deleteBtn = commentBox.querySelector('.delete-btn');
              deleteBtn.addEventListener('click', async () => {
                if (!confirm("댓글을 삭제하시겠습니까?")) return;

                try {
                  const res = await fetch(`/api/comments/${comment.commentsId}`, {
                    method: 'DELETE'
                  });

                  const result = await res.json();

                  if (result.header?.rtcd === 'S00') {
                    alert("삭제 완료!");
                    loadComments(boardId); // 목록 다시 불러오기
                  } else {
                    alert("삭제 실패: " + result.header?.rtmsg);
                  }
                } catch (err) {
                  console.error("삭제 실패:", err);
                  alert("서버 오류로 삭제에 실패했습니다.");
                }
              });

              //댓글 수정기능 추가
              const editBtn = commentBox.querySelector('.edit-btn');

              editBtn.addEventListener('click', () => {
                const originalContent = comment.content;
                const originalBox = commentBox.innerHTML;

                // 수정 모드로 전환
                commentBox.innerHTML = `
                  <div class="comment-writer">${comment.writer}</div>
                  <textarea type="text" class="comment-content edit-content">${comment.content}</textarea>
                  <div class="btn-comment-group">
                    <button class="btn save-btn">저장</button>
                    <button class="btn cancel-btn">취소</button>
                  </div>
                `;

                const saveBtn = commentBox.querySelector('.save-btn');
                const cancelBtn = commentBox.querySelector('.cancel-btn');
                const textarea = commentBox.querySelector('.edit-content');

                // 저장 클릭 시
                saveBtn.addEventListener('click', async () => {
                  const updatedContent = textarea.value;
                  if (!updatedContent) {
                    alert("내용을 입력하세요.");
                    return;
                  }

                  try {
                    const res = await fetch(`/api/comments/${comment.commentsId}`, {
                      method: "PATCH",
                      headers: { "Content-Type": "application/json" },
                      body: JSON.stringify({ content: updatedContent })
                    });

                    const result = await res.json();
                    if (result.header?.rtcd === "S00") {
                      alert("수정 완료!");
                      loadPagedComments(boardId, 1);
                    } else {
                      alert("수정 실패: " + result.header?.rtmsg);
                    }
                  } catch (err) {
                    console.error("수정 실패:", err);
                    alert("서버 오류 발생");
                  }
                });

                // 취소 클릭 시 → 원래대로 복구
                cancelBtn.addEventListener('click', () => {
                  commentBox.innerHTML = originalBox;
                  loadPagedComments(boardId, 1); // 또는 그대로 원래 댓글 다시 그려도 OK
                });
              });
        });

        drawPagination(boardId, paging.pageNo, paging.totalCount, paging.numOfRows, 5);
    } catch (error) {
        console.error("댓글 조회 실패: ", error);
    }
}


//댓글 등록
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
        //   configCommentPagination(boardId); // 페이징 포함해서 첫 페이지부터 다시 보여주기
          loadPagedComments(boardId, 1)
        } else {
          document.getElementById("error_reply").textContent =
            "댓글 등록 실패!";
        }
      });
  });


  //댓글 작성 취소
document.addEventListener("DOMContentLoaded", () => {
  const $cancelBtn = document.getElementById("btnCancel_reply");
  const $writer = document.getElementById("writer");
  const $content = document.getElementById("content");

  $cancelBtn.addEventListener("click", () => {
    $writer.value = "";
    $content.value = "";
  });
});


//댓글 페이징 버튼
//function drawPagination(boardId, currentPage, totalCount, numOfRows) {
//  const $pagination = document.getElementById("reply_pagination");
//  $pagination.innerHTML = "";
//
//  const totalPages = Math.ceil(totalCount / numOfRows);
//
//  for (let i = 1; i <= totalPages; i++) {
//    const btn = document.createElement("button");
//    btn.textContent = i;
//    btn.className = (i === currentPage) ? "active-page" : "";
//    btn.addEventListener("click", () => {
//      loadPagedComments(boardId, i);
//    });
//    $pagination.appendChild(btn);
//  }
//}

function drawPagination(boardId, currentPage, totalCount, numOfRows, pageBlockSize = 5) {
  const $pagination = document.getElementById("reply_pagination");
  $pagination.innerHTML = "";

  const totalPages = Math.ceil(totalCount / numOfRows);

  if (totalPages <= 1) return; // 페이지가 1개 이하면 페이징 생략

  // 1. 시작, 끝 페이지 계산
  const blockStart = Math.floor((currentPage - 1) / pageBlockSize) * pageBlockSize + 1;
  const blockEnd = Math.min(blockStart + pageBlockSize - 1, totalPages);

  // 2. "처음" 버튼
  if (blockStart > 1) {
    const firstBtn = makePageButton("<<", () => loadPagedComments(boardId, 1));
    $pagination.appendChild(firstBtn);
  }

  // 3. "이전" 버튼
  if (blockStart > 1) {
    const prevBtn = makePageButton("<", () => loadPagedComments(boardId, blockStart - 1));
    $pagination.appendChild(prevBtn);
  }

  // 4. 페이지 번호 버튼
  for (let i = blockStart; i <= blockEnd; i++) {
    const btn = makePageButton(i, () => loadPagedComments(boardId, i));
    if (i === currentPage) btn.classList.add("active-page");
    $pagination.appendChild(btn);
  }

  // 5. "다음" 버튼
  if (blockEnd < totalPages) {
    const nextBtn = makePageButton(">", () => loadPagedComments(boardId, blockEnd + 1));
    $pagination.appendChild(nextBtn);
  }

  // 6. "끝" 버튼
  if (blockEnd < totalPages) {
    const lastBtn = makePageButton(">>", () => loadPagedComments(boardId, totalPages));
    $pagination.appendChild(lastBtn);
  }

  function makePageButton(text, onClick) {
    const btn = document.createElement("button");
    btn.textContent = text;
    btn.addEventListener("click", onClick);
    return btn;
  }
}