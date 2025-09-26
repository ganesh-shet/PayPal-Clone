package com.paypal.gatewayserver.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    private static final String SECRET = "secret83108413809980315858ganeshshet1234gshet";

    private static Key getSigniningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public static Claims validateToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigniningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
