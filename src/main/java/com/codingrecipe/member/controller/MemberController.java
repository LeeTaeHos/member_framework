package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member") // 공통주소를 표현
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //    @GetMapping("/member/save") // 공통주소를 해놨기때문에 /member 안써도 됨
    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
    int saveResult = memberService.save(memberDTO);
    if(saveResult > 0){
        return "login";
    }else {
        return "save";
    }
    }
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute MemberDTO memberDTO,
                        HttpSession session){
        boolean loginResult = memberService.login(memberDTO);
        if(loginResult){
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "main";
        }else {
            return "login";
        }
    }

    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    // /member?id=1
    @GetMapping
    public String findById(@RequestParam("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "deltail";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }

    // 수정 화면 요청
    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        // 세션에 저장된 내 이메일 가져오기
       String loginEmail = (String)session.getAttribute("loginEmail");
       MemberDTO memberDTO = memberService.finByMemberEmail(loginEmail);
       model.addAttribute("member", memberDTO);
       return "update";
    }

    // 수정 처리
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        boolean result = memberService.update(memberDTO);
        if(result){
            return "redirect:/member?id="+ memberDTO.getId();
        }else {
            return "index";
        }
    }
}