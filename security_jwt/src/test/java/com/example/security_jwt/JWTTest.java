package com.example.security_jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Base64;
//import java.util.Map;

public class JWTTest {
    public void printToken (String token) {
        String[] tokens = token.split("\\.");
        System.out.println("token header => " + new String(Base64.getDecoder().decode(tokens[0])));
        System.out.println("token body => " + new String(Base64.getDecoder().decode(tokens[1])));
    }

//    @DisplayName("1. jjwt 를 이용한 토큰테스트(okta token)")
//    @Test
//    void jjwtTest() {
//        String okta_token = Jwts.builder().addClaims(
//                Map.of("key", "park", "value", "2000")
//        )
//        .signWith(SignatureAlgorithm.HS256, "park")
//        .compact();
//        printToken(okta_token);
//
//        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey("park").parseClaimsJws(okta_token);
//        System.out.println("tokenInfo => " + tokenInfo);
//    }

    @DisplayName("2. java-jwt를 이용한 토큰테스트(oauth token)")
    @Test
    void javaJwtTest() {
        String oauth0_token = JWT.create()
                .withClaim("key", "park")
                .withClaim("value", "20000")
                .sign(Algorithm.HMAC256("park"));
        printToken(oauth0_token);

        DecodedJWT tokenInfo = JWT.require(Algorithm.HMAC256("park")).build().verify(oauth0_token);
        System.out.println("tokenInfo => " + tokenInfo.getClaims());
    }

    @DisplayName("3. 만료시간")
    @Test
    void expiredTest() throws InterruptedException {
        final Algorithm ALG = Algorithm.HMAC256("park");
        String token = JWT.create()
                .withSubject("park1234")
                .withNotBefore(new Date(System.currentTimeMillis() + 1000))
                .withExpiresAt(new Date(System.currentTimeMillis() + 3000))
                .sign(ALG);

        Thread.sleep(500);

        DecodedJWT verify = JWT.require(ALG).build().verify(token);
        System.out.println(verify.getClaims());
    }
}
