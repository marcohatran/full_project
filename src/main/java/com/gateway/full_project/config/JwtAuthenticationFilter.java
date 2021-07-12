package com.gateway.full_project.config;

import com.gateway.full_project.service.JwtUserDetailService;
import com.gateway.full_project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwt;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //get jwt header //sarer from bearer then validate

        String requestTokenHander=request.getHeader("Authorization");
        String username=null;
        String jwtToken=null;
        if(requestTokenHander!=null && requestTokenHander.startsWith("Bearer ")) {
            jwtToken=requestTokenHander.substring(7);

            try {
                username=this.jwt.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            UserDetails userDetails=this.jwtUserDetailService.loadUserByUsername(username);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

                UsernamePasswordAuthenticationToken usernamePasswordAuth=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);

            }else {
                System.out.println("is not valid");
            }
        }
        filterChain.doFilter(request, response);

    }
}
