package com.alex.springrest.security;

import com.alex.springrest.SpringApplicationContext;
import com.alex.springrest.dto.UserDto;
import com.alex.springrest.entities.UserEntity;
import com.alex.springrest.models.request.UserLoginRequestModel;
import com.alex.springrest.models.response.UserRest;
import com.alex.springrest.services.UsersService;
import com.alex.springrest.shared.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);

            return authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      creds.getEmail(),
                      creds.getPassword(),
                      new ArrayList<>()
              )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UsersService usersService = (UsersService) SpringApplicationContext.getBean("usersServiceImpl");
        UserDto userDto = usersService.findByEmail(username);
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(userDto, userRest);

        response.addHeader("Content-Type", "application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginResponse(token, userRest)));
    }
}
