package com.community.world.controller.member;


import com.community.world.config.jwt.TokenService;
import com.community.world.dto.jwt.TokenDto;
import com.community.world.dto.member.MemberDto;
import com.community.world.service.MemberApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberViewController {
    private final MemberApiService memberApiService;

    public MemberViewController(
            MemberApiService memberApiService,
            TokenService tokenService) {
        this.memberApiService = memberApiService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(
            @ModelAttribute MemberDto.Request request) {
        memberApiService.addMember(request);
        return "redirect:/login";
    }

}
