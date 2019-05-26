package com.scrumboard.app.auth;

import com.scrumboard.app.session.Session;
import com.scrumboard.app.session.SessionRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
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
        return session.filter(value -> !value.getExpiryDate().before(new Date())).isPresent();

    }
}
