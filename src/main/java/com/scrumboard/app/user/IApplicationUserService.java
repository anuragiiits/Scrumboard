package com.scrumboard.app.user;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IApplicationUserService {
    ResponseEntity<String> addUser(ApplicationUser user);
    Optional<ApplicationUser> findByUsername(String username);
    ResponseEntity<String> logout(String token);
}
