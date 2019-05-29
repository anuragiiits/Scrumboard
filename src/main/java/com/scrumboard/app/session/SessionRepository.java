package com.scrumboard.app.session;

import com.scrumboard.app.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByToken(String token);
    Optional<Session> findByCreatedBy(ApplicationUser applicationUser);
}
