package com.community.world.controller.member;

import com.community.world.config.jwt.TokenService;
import com.community.world.domain.Member;
import com.community.world.dto.jwt.TokenDto;
import com.community.world.dto.member.MemberDto;
import com.community.world.service.MemberApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
public class MemberApiController {

    private final MemberApiService memberApiService;
    private final TokenService tokenService;

    public MemberApiController(
            MemberApiService memberApiService,
            TokenService tokenService) {
        this.memberApiService = memberApiService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody MemberDto.Request request) {
        System.out.println("login Controller : " + request.getEmail());

        MemberDto.Response response = memberApiService.loginMember(request);
        String token = tokenService.generateToken(response);
        TokenDto.Response tokenDtoResponse = TokenDto.Response
                .builder()
                .token(token)
                // authentication으로 정보 찾게되면 삭제 가능
                .memberId(response.getId())
                .build();
        return ResponseEntity.ok(tokenDtoResponse);
    }

    //회원 검색
    @PostMapping("/search-member")
    public ResponseEntity<?> findMember(
            @RequestBody MemberDto.Request request,
            Authentication authentication
    ) {

        List<MemberDto.Response> response =
                memberApiService.findMemberEmail(request,
                        (Member)authentication.getPrincipal());
        response.forEach(m -> System.out.println(m.getName()));
        return ResponseEntity.ok(response);
    }
}
