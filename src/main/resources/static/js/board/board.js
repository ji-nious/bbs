// 초기 로딩
document.addEventListener("DOMContentLoaded", () => {
  loadBoardList(1);
});

// 게시글 목록 불러오기
async function loadBoardList(pageNo = 1) {
  const res = await fetch(`/api/boards/paging?pageNo=${pageNo}&numOfRows=10`);
  const json = await res.json();
  const boards = json.body;
  const paging = json.paging;

  renderTable(boards);
  renderPagination(paging);
}

// 테이블 렌더링
function renderTable(boards) {
  const $tbody = document.getElementById("board-list");
  $tbody.innerHTML = "";

  boards.forEach(board => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${board.boardId}</td>
      <td><a href="/boards/${board.boardId}">${board.title}</a></td>
      <td>${board.writer}</td>
      <td>${formatDate(board.createdAt)}</td>
      <td>${formatDate(board.updatedAt)}</td>
    `;
    $tbody.appendChild(row);
  });
}

// 날짜 포맷터
function formatDate(dateTime) {
  return dateTime ? dateTime.replace('T', ' ').substring(0, 19) : '';
}

// 페이징 렌더링
function renderPagination(paging) {
  const $pagination = document.getElementById("pagination");
  const { pageNo, numOfRows, totalCount } = paging;
  const totalPages = Math.ceil(totalCount / numOfRows);

  const blockSize = 10; // 한 블록에 보여줄 페이지 수
  const currentBlock = Math.ceil(pageNo / blockSize);
  const startPage = (currentBlock - 1) * blockSize + 1;
  const endPage = Math.min(startPage + blockSize - 1, totalPages);

  let html = '';

  if (pageNo > 1) {
    html += `<button class="page-btn" data-page="1">처음</button>`;
    html += `<button class="page-btn" data-page="${pageNo - 1}">이전</button>`;
  }

  for (let i = startPage; i <= endPage; i++) {
    html += `<button class="page-btn ${i === pageNo ? 'active' : ''}" data-page="${i}">${i}</button>`;
  }

  if (pageNo < totalPages) {
    html += `<button class="page-btn" data-page="${pageNo + 1}">다음</button>`;
    html += `<button class="page-btn" data-page="${totalPages}">끝</button>`;
  }

  $pagination.innerHTML = html;

  document.querySelectorAll(".page-btn").forEach(btn => {
    btn.addEventListener("click", e => {
      const page = parseInt(e.target.dataset.page);
      loadBoardList(page);
    });
  });
}
