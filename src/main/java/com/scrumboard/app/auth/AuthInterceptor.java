package com.scrumboard.app.auth;

import com.scrumboard.app.exception.AccessDeniedException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    UserAuthHandler userAuthHandler;

    public AuthInterceptor(UserAuthHandler userAuthHandler) {
        this.userAuthHandler = userAuthHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(HttpMethod.valueOf(request.getMethod()).equals(HttpMethod.OPTIONS)) {
            return true;
        }

         if(userAuthHandler.authenticateRequest(request)) {
             return true;
         }
         throw new AccessDeniedException("Not Allowed");
    }
}
