package com.community.world.controller.oauth;

import com.community.world.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/oauth")
public class OauthController {

    private static final Logger log = LoggerFactory.getLogger(OauthController.class);
    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/{platform}")
    public void oauthLoginRequest(
            @PathVariable(name = "platform") String platform,
            HttpServletResponse response) throws IOException {
        log.info("oauthLoginRequest 들어옴");
        String uri = oauthService.oauthLoginRequest(platform);
        log.info("uri 주소 : {} ", uri);
        response.sendRedirect(uri);
    }

    @GetMapping("/{platform}/callback")
    public void oauthLoginRedirect(
            HttpServletResponse response,
            @PathVariable("platform")
            String platform,
            @RequestParam(name = "code", required = false)
            String code,
            @RequestParam(name = "state", required = false)
            String state,
            @RequestParam(name = "error", required = false)
            String error,
            @RequestParam(value = "error_description", required = false)
            String errorDescription) throws Exception {

        log.info("oauthLoginRedirect 들어옴");
        if (error != null)
            throw new Exception(errorDescription);

        log.info("code : {} , state : {} ", code, state);

        var tokenDtoResponse = oauthService
                .oauthLoginRedirect(platform, code, state);

        response.sendRedirect("/login?token="+tokenDtoResponse.getToken());
    }
}
