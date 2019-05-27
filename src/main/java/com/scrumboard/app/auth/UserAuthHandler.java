package com.scrumboard.app.auth;

import com.scrumboard.app.session.Session;
import com.scrumboard.app.session.SessionRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthHandler {

    SessionRepository sessionRepository;

    public UserAuthHandler(SessionRepository sessionRepository)  {
        this.sessionRepository = sessionRepository;
    }

    public Boolean authenticateRequest(HttpServletRequest request) {
        String accessToken = request.getHeader("access_token");
        if(Strings.isEmpty(accessToken)) {
            return false;
        }
        Optional<Session> session = sessionRepository.findByToken(accessToken);
        if(session.filter(value -> !value.getExpiryDate().before(new Date())).isPresent()){
            request.setAttribute("username", session.get().getCreatedBy().getUsername());
            return true;
        }
        return false;
    }
}
