package com.community.world.config.jwt;

import com.community.world.dto.member.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {
    SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(MemberDto.Response response) {
        var now = new Date();
        var exp = new Date(now.getTime() + (60 * 60 * 1000));
        return createToken(response, now, exp);
    }

    private String createToken(
            MemberDto.Response response, Date now, Date exp) {
        return Jwts.builder()
                .subject(response.getEmail())
                .claim("memberName", response.getName())
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        }catch (JwtException e) {
            throw e;
        }
        return true;
    }

    public String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (JwtException e) {
            throw e;
        }
    }

    public String getMemberName(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("memberName", String.class);

        }catch (JwtException e) {
            throw e;
        }
    }
}
