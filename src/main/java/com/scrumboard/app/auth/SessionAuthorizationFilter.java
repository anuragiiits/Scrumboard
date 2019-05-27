package com.scrumboard.app.auth;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.scrumboard.app.auth.SecurityConstants.*;

public class SessionAuthorizationFilter extends BasicAuthenticationFilter {

    private UserAuthHandler userAuthHandler;

    public SessionAuthorizationFilter(AuthenticationManager authManager, UserAuthHandler userAuthHandler) {
        super(authManager);
        this.userAuthHandler = userAuthHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        if(userAuthHandler.authenticateRequest(request)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        else{
            chain.doFilter(request, response);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String user = (String)request.getAttribute("username");

        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }
        return null;

    }
}