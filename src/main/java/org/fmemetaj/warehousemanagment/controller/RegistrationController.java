package org.fmemetaj.warehousemanagment.controller;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.RegistrationForm;
import org.fmemetaj.warehousemanagment.security.JwtService;
import org.fmemetaj.warehousemanagment.security.Token;
import org.fmemetaj.warehousemanagment.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/signup")
public class RegistrationController {

    private final UserServiceImpl userServiceImpl;
    private final JwtService jwtService;

    public RegistrationController(UserServiceImpl userServiceImpl, JwtService jwtService) {
        this.userServiceImpl = userServiceImpl;
        this.jwtService = jwtService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Token> signUp(@RequestBody RegistrationForm registrationForm) {
        log.info("Received registration form: {}", registrationForm);

        var user = userServiceImpl.createUser(registrationForm);

        var jwtToken = jwtService.createJwtToken(user);

        return ResponseEntity.ok(Token.fromJwt(jwtToken));
    }
}
