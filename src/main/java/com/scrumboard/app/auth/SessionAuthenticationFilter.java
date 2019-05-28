package com.scrumboard.app.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrumboard.app.user.request.SignupRequest;
import com.scrumboard.app.session.ISessionService;
import com.scrumboard.app.session.Session;
import com.scrumboard.app.user.IApplicationUserService;
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
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import static com.scrumboard.app.auth.SecurityConstants.*;

public class SessionAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private ISessionService sessionService;
    private IApplicationUserService applicationUserService;

    public SessionAuthenticationFilter(AuthenticationManager authenticationManager, ISessionService sessionService, IApplicationUserService applicationUserService) {
        this.authenticationManager = authenticationManager;
        this.sessionService = sessionService;
        this.applicationUserService = applicationUserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            SignupRequest signupRequest = new ObjectMapper()
                    .readValue(req.getInputStream(), SignupRequest.class);

            String[] credentials = new String(Base64.getDecoder()
                    .decode(signupRequest.getCredentials()
                            .getBytes())).split(":");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials[0],
                            credentials[1],
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = UUID.randomUUID().toString();
        Session session = new Session();
        session.setToken(token);
        session.setStatus(true);
        session.setCreatedBy(applicationUserService.findByUsername((String)(((User) auth.getPrincipal()).getUsername())).get());       //TODO: Check for the getUsername()
        session.setExpiryDate();
        sessionService.createSession(session);

        res.addHeader(HEADER_STRING, token);
    }
}
