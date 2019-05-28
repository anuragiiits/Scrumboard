package com.scrumboard.app.session;

import java.util.Optional;

public interface ISessionService {

    Optional<Session> findByToken(String accessToken);
    void createSession(Session session);
}
