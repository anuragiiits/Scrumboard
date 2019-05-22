package com.scrumboard.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    @Autowired
    private IApplicationUserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userService.addUser(user);
    }

}
