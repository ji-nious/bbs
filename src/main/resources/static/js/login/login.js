// logout 함수
document.addEventListener("DOMContentLoaded", function () {
    const btnLogout = document.getElementById("btnLogout");

    if (btnLogout) {
      btnLogout.addEventListener("click", logout);
    }

    function logout() {
      fetch("/logout", {
        method: "DELETE"
      })
        .then(res => res.json())
        .then(data => {
          if (data.header?.rtcd === "S00") {
            alert("로그아웃되었습니다.");
            location.href = "http://localhost:9090/";
          } else {
            alert("로그아웃 실패: " + data.header?.rtmsg);
          }
        })
        .catch((err) => {
          console.error("로그아웃 오류:", err);
        });
    }
  });