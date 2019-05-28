package com.scrumboard.app.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService implements ISessionService{

    @Autowired
    private SessionRepository sessionRepository;

    public Optional<Session> findByToken(String accessToken) {
        return sessionRepository.findByToken(accessToken);
    }

    public void createSession(Session session){
        sessionRepository.save(session);
    }

}
