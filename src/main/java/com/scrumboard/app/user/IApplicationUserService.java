package com.scrumboard.app.user;

import org.springframework.http.ResponseEntity;

public interface IApplicationUserService {
    ResponseEntity<String> addUser(ApplicationUser user);
}
