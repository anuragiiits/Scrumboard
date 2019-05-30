package com.scrumboard.app.user;


import com.scrumboard.app.exception.BadRequestException;
import com.scrumboard.app.exception.ResourceNotFoundException;
import com.scrumboard.app.session.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserService implements IApplicationUserService, UserDetailsService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public ResponseEntity<String> addUser(ApplicationUser user) {

        Optional<ApplicationUser> existingUser = applicationUserRepository.findByUsername(user.getUsername());
        if(existingUser.isPresent()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            throw new BadRequestException("Username already taken.");
        }

        applicationUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ApplicationUser> applicationUser = applicationUserRepository.findByUsername(username);
        if (!applicationUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        return new User(applicationUser.get().getUsername(), applicationUser.get().getPassword(), emptyList());
    }

    public Optional<ApplicationUser> findByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username);
    }

    public ResponseEntity<String> logout(String token){

        sessionRepository.findByToken(token).map(session -> {
                     session.setStatus(false);
                     return sessionRepository.save(session);
            });

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
