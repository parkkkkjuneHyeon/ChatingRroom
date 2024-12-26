package com.community.world.service;


import com.community.world.client.ClientList;
import com.community.world.client.ClientStrategy;
import com.community.world.client.OauthClient;
import com.community.world.config.jwt.TokenService;
import com.community.world.dto.jwt.TokenDto;
import com.community.world.dto.member.MemberDto;
import com.community.world.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OauthService {

    private final ClientStrategy clientStrategy;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public OauthService(
            ClientStrategy clientStrategy,
            MemberRepository memberRepository,
            TokenService tokenService
            ) {
        this.clientStrategy = clientStrategy;
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    private static final Logger log = LoggerFactory
            .getLogger(OauthService.class);

    public String oauthLoginRequest(String platform) {
        OauthClient oauthClient = clientStrategy
                .getOauthClient(ClientList.getClient(platform));
        return oauthClient.oauthLoginRequest();
    }

    public TokenDto.Response oauthLoginRedirect(
            String platform, String code, String state) {
        OauthClient oauthClient = clientStrategy
                .getOauthClient(ClientList.getClient(platform));

        var tokenResponse = oauthClient.requestAccessToken(code, state);

        var member = tokenResponse.getMember();
        //구글에서 가져온 사용자 정보를 가지고 새로운 jwt토큰을 만들어서 전달
        var token = tokenService
                .generateToken(MemberDto.Response.getMemberResponse(member));
        
        if(memberRepository.findByEmail(member.getEmail()).isEmpty()) {
            member = memberRepository.save(member);
        }

        return TokenDto.Response.builder()
                .token(token)
                .memberId(member.getId())
                .build();
    }



}
