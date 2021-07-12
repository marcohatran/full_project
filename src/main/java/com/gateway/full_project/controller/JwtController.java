package com.gateway.full_project.controller;

import com.gateway.full_project.model.JwtRequest;
import com.gateway.full_project.model.JwtResponse;
import com.gateway.full_project.service.JwtUserDetailService;
import com.gateway.full_project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {
    @Autowired
    private JwtUtil jwt;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value="/authentication",method = RequestMethod.POST)
    ResponseEntity<?> genRateToken(@RequestBody JwtRequest jwtRequest){

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new BadCredentialsException("Bad Credential");
        }
        UserDetails userDetails=this.jwtUserDetailService.loadUserByUsername(jwtRequest.getUsername());
        String data=this.jwt.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(data));

    }
}
