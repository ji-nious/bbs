//회원가입 양식 유효성 검사 - 프론트
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const errorBox = document.getElementById("globalError");

    form.addEventListener("submit", function (e) {
        const email = form.querySelector("[name='email']").value.trim();
        const passwd = form.querySelector("[name='passwd']").value.trim();
        const passwdChk = form.querySelector("[name='passwdChk']").value.trim();
        const nickname = form.querySelector("[name='nickname']").value.trim();

        // 기존 에러 메시지 초기화
        errorBox.innerHTML = "";

        // 이메일 형식
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            showError("올바른 이메일 형식을 입력하세요.");
            e.preventDefault();
            return;
        }

        // 비밀번호: 숫자만, 4~8자리
        const pwRegex = /^\d{4,8}$/;
        if (!pwRegex.test(passwd)) {
            showError("비밀번호는 숫자로만 4~8자리여야 합니다.");
            e.preventDefault();
            return;
        }

        // 비밀번호 확인
        if (passwd !== passwdChk) {
            showError("비밀번호가 일치하지 않습니다.");
            e.preventDefault();
            return;
        }

        // 닉네임: 한글 10자 이하 또는 영어 15자 이하
        const nickKor = /^[가-힣]{1,10}$/;
        const nickEng = /^[a-zA-Z]{1,15}$/;
        if (!(nickKor.test(nickname) || nickEng.test(nickname))) {
            showError("닉네임은 한글 10자 이하 또는 영어 15자 이하로 입력하세요.");
            e.preventDefault();
            return;
        }

        // 에러 메시지 표시 함수
        function showError(msg) {
            errorBox.innerHTML = `<p class="global-err">${msg}</p>`;
        }
    });
});


//회원가입 취소 시 인덱스 페이지로
document.addEventListener("DOMContentLoaded", function () {
    const cancelBtn = document.getElementById("btnCancel");
    if (cancelBtn) {
        cancelBtn.addEventListener("click", function () {
            window.location.href = "/";  // index 페이지로 이동
        });
    }
});