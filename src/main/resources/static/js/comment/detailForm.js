//수정
        const $btnUpdate = document.getElementById("btnUpdate");
        $btnUpdate.addEventListener(
          "click",
          (e) => {
            const $boardId = document.getElementById("boardId");
            const boardId = $boardId.value;

            console.log(boardId);

            location.href = `/boards/${boardId}/edit`; //GET http://localhost:9080/boards/18/edit
          },
          false
        );

        //삭제
        const $btnDelete = document.getElementById("btnDelete");
        $btnDelete.addEventListener(
          "click",
          (e) => {
            const $boardId = document.getElementById("boardId");
            const id = $boardId.value;

            const $modalDel = document.getElementById("modalDel");
            const $btnYes = document.querySelector(".btnYes");
            const $btnNo = document.querySelector(".btnNo");

            //모달창 띄우기
            $modalDel.showModal();

            // 모달창 닫힐 때
            $modalDel.addEventListener("close", (e) => {
              console.log($modalDel.returnValue);
              if ($modalDel.returnValue == "yes") {
                location.href = `/boards/${id}/del`;
              }
            });

            // 예 버튼 클릭 시
            $btnYes.addEventListener("click", (e) => {
              $modalDel.close("yes"); //yes값을 반환하며 모달창을 닫음
            });

            // 아니오 버튼 클릭 시
            $btnNo.addEventListener("click", (e) => {
              $modalDel.close("no");
            });
          },
          false
        );

        //목록
        const $btnFindAll = document.getElementById("btnFindAll");
        $btnFindAll.addEventListener(
          "click",
          (e) => {
            location.href = "/boards"; //get http://localhost:9090/boards
          },
          false
        );