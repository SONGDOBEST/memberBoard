package com.icia.memberboard.controller;

import com.icia.memberboard.dto.MemberDTO;
import com.icia.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm(){
        return "memberPages/memberSave";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException{
        memberService.save(memberDTO);
        return "memberPages/loginPage";
    }

    @PostMapping("/dup-check")
    public ResponseEntity emailCheck(@RequestBody MemberDTO memberDTO){
        boolean result = memberService.emailCheck(memberDTO.getMemberEmail());
        if (result){
            return new ResponseEntity<>("사용가능", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("사용불가", HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/login")
    public String loginForm(){
        return "memberPages/loginPage";
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberDTO memberDTO, HttpSession session, HttpSession session2){
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null){
            session.setAttribute("loginMember", loginResult);
            return new ResponseEntity<>("로그인성공", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("로그인실패", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/memberList";
    }
    @GetMapping("/mypage")
    public String mypage(@RequestParam("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
