package org.fmemetaj.warehousemanagment.controller;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.RegistrationForm;
import org.fmemetaj.warehousemanagment.security.JwtService;
import org.fmemetaj.warehousemanagment.security.Token;
import org.fmemetaj.warehousemanagment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/signup")
public class RegistrationController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Token> signUp(@RequestBody RegistrationForm registrationForm) {
        log.info("Received registration form: {}", registrationForm);

        var user = userService.createUser(registrationForm, passwordEncoder);

        var jwtToken = jwtService.createJwtToken(user);

        return ResponseEntity.ok(Token.fromJwt(jwtToken));
    }
}
