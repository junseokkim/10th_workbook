package com.umc._th_workbook.domain.member.controller;

import com.umc._th_workbook.domain.member.entity.Role;
import com.umc._th_workbook.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signUp(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam Role role
    ) {
        memberService.registerNewMember(username, password, role);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}