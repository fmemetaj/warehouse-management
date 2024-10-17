package org.fmemetaj.warehousemanagment.security;

import org.fmemetaj.warehousemanagment.service.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WarehouseAuthenticationManager implements AuthenticationManager {

    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public WarehouseAuthenticationManager(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = (String) authentication.getPrincipal();
        var password = (String) authentication.getCredentials();

        var user = userServiceImpl.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unknown username: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return user;
    }
}
