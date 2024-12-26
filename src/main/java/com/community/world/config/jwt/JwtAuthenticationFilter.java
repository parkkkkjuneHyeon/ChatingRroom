package com.community.world.config.jwt;


import com.community.world.service.MemberApiService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String BEARER = "Bearer ";
    private final String AUTHORIZATION = HttpHeaders.AUTHORIZATION;
    private final MemberApiService memberApiService;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(
            MemberApiService memberApiService,
            TokenService tokenService) {

        this.memberApiService = memberApiService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(AUTHORIZATION);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        log.info("필터 진입 헤더 : {}", authorization);
        log.info("요청 url  : {}", request.getRequestURI());

        if(!ObjectUtils.isEmpty(authorization)
                && securityContext.getAuthentication() == null
                && authorization.startsWith(BEARER)) {

            String accessToken = authorization
                    .substring(BEARER.length());
            log.info("accessToken : {}",accessToken);
            log.info("get email : {}",tokenService.getSubject(accessToken));

            String memberEmail = tokenService.getSubject(accessToken);

            log.info("필터 진입 후 아이디 : {}", memberEmail);

            var userDetails = memberApiService.loadUserByUsername(memberEmail);
            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));

            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            log.info("토큰 인증 완료");
        }

        filterChain.doFilter(request,response);
    }
}
