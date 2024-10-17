package org.fmemetaj.warehousemanagment.controller;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.security.JwtService;
import org.fmemetaj.warehousemanagment.security.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final JwtService jwtService;

    public AuthenticationController(final JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<?> login(Principal principal) {
        try {
            var user = (User) principal;

            var jwtToken = jwtService.createJwtToken(user);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
                    .body(Token.fromJwt(jwtToken));
        } catch (BadCredentialsException badCredentialsException) {
            log.error("Login Error", badCredentialsException);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
