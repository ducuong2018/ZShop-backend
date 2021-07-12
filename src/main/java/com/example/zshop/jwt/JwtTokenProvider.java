package com.example.zshop.jwt;

import com.example.zshop.data.TokenInfo;
import io.jsonwebtoken.*;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    private String secret  = "cuongdc";

    private int tokenExpireTime = 6400;

    private int refreshExpireTime= 64000;;


    public String generateToken(TokenInfo tokenInfo) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, tokenInfo.getUserId().toString());
    }

    public String generateRefreshToken(TokenInfo tokenInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", tokenInfo.getUserId().toString());
        return doGenerateRefreshToken(claims, tokenInfo.getUserId().toString());
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpireTime * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date date= new Date(System.currentTimeMillis());

        log.info(String.valueOf(date));
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public TokenInfo validateToken(String accessToken) {
        Claims claims = getAllClaimsFromToken(accessToken);
        Date expiration = claims.getExpiration();
        if(expiration.before(new Date()))
            return null;

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(Long.valueOf(claims.get(Claims.SUBJECT, String.class)));
        return tokenInfo;
    }
}
