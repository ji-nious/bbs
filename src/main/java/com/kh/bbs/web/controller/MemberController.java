package com.kh.bbs.web.controller;

import com.kh.bbs.domain.entity.Member;
import com.kh.bbs.domain.member.svc.MemberSVC;
import com.kh.bbs.web.form.member.JoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberSVC memberSVC;

  //가입화면
  @GetMapping("/join")        // GET /members/join
  public String joinForm(Model model) {

    model.addAttribute("joinForm", new JoinForm());
    //Model : joinForm 객체에 데이터를 담아서 view단에 넘겨주는 역할(컨트롤러->뷰)
    //view단에선 이 객체를 넘겨받아 ${joinForm.xxx} 식으로 타임리프가 객체 내 데이터에 접근 가능
    return "member/joinForm";

  }

  //가입처리
  @PostMapping("/join")     // POST /members/join
  public String join(
      @ModelAttribute JoinForm joinForm,
      //@ModelAttribute : form 데이터를 자바 객체에 주입(클라이언트->컨트롤러)
      BindingResult bindingResult
      //BindingResult : 검증 결과(에러) 담음. 폼 데이터를 객체에 바인딩하거나, @Valid 할 때 에러 발생하면 이 정보 담음
  ) {

    log.info("joinForm={}", joinForm);

    //유효성 체크
    //1) 필드오류
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "member/joinForm";
    }

    //2) 글로벌 오류
    if (!joinForm.getPasswd().equals((joinForm.getPasswdChk()))) {
      bindingResult.reject("passwdErr", "비밀번호가 일치하지 않습니다.");
    }
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "member/joinForm";
    }

    //회원가입 정상로직 처리
    Member member = new Member();
    BeanUtils.copyProperties(joinForm, member);

    //클라이언트에서 받은 데이터 Member member객체에 옮겨 받은 걸 실제 DB에 넣기
    Member joinedMember = memberSVC.join(member);

    return "redirect:/login";   //302 GET http://localhost:9090/login
  }


}
