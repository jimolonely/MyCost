package com.jimo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jimo on 18-7-1.
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    private final String base64EncodedSecurityKey = "sdafkjsadjawifn";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            throw new ServletException("Invalid Authorization Header");
        }
        final String token = header.substring(7);
        checkToken(token);
        return true;
    }

    private void checkToken(String token) throws ServletException {
        try {
            final Claims claims = Jwts.parser().setSigningKey(base64EncodedSecurityKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e1) {
            throw new ServletException("token expired");
        } catch (Exception e2) {
            throw new ServletException("other token error");
        }
    }
}
