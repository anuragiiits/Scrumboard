package com.scrumboard.app.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserService implements IApplicationUserService, UserDetailsService {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    public ResponseEntity<String> addUser(ApplicationUser user) {

        ApplicationUser existingUser = applicationUserRepository.findByUsername(user.getUsername());
        if(existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        applicationUserRepository.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
