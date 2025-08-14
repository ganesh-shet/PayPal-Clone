package com.paypal.user_service.Util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtRequestFilter extends OncePerRequestFilter {
    private  final JwtUtil jwtUtil;
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //Extracting the username
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try{
                username = jwtUtil.extractUsername(jwt);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Not Authenticated user--->Do token validation
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if(jwtUtil.validateToken(jwt,username)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Authorization --> RBAC
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            if(jwt == null || jwt.isBlank()){
                filterChain.doFilter(request,response);
                return;// Skip Processing if token is NULL
            }
            try{
                username = jwtUtil.extractUsername(jwt);
                String role = jwtUtil.extractRole(jwt);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
                        null,
                        List.of(new SimpleGrantedAuthority(role)));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request,response);
            }catch (Exception e){
                e.printStackTrace(); // log the error
            }
        }else{
            filterChain.doFilter(request,response);
            return;
        }
    }


}
