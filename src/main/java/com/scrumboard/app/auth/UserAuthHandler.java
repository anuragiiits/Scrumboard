package com.scrumboard.app.auth;

import com.scrumboard.app.session.ISessionService;
import com.scrumboard.app.session.Session;
import com.scrumboard.app.session.SessionService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthHandler {

    @Autowired
    ISessionService sessionService;

    public UserAuthHandler(SessionService sessionService)  {
        this.sessionService = sessionService;
    }

    public Boolean authenticateRequest(HttpServletRequest request) {
        String accessToken = request.getHeader("access_token");
        if(Strings.isEmpty(accessToken)) {
            return false;
        }
        Optional<Session> session = sessionService.findByToken(accessToken);
        if(session.filter(value -> value.getStatus() && !value.getExpiryDate().before(new Date())).isPresent()){
            request.setAttribute("username", session.get().getCreatedBy().getUsername());
            return true;
        }
        return false;
    }
}
