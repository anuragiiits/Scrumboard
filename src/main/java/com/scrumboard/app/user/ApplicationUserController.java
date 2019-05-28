package com.scrumboard.app.user;

import com.scrumboard.app.user.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Base64;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Transactional(rollbackOn = Exception.class)
public class ApplicationUserController {

    @Autowired
    private IApplicationUserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignupRequest signupRequest) {

        String[] credentials = new String(Base64.getDecoder()
                .decode(signupRequest.getCredentials()
                        .getBytes())).split(":");

        String username = credentials[0];
        String password = credentials[1];
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(username);
        applicationUser.setPassword(bCryptPasswordEncoder.encode(password));

        return userService.addUser(applicationUser);
    }

    @GetMapping("logout")
    public ResponseEntity<String> logout(){
        return userService.logout();
    }
}
