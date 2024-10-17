package org.fmemetaj.warehousemanagment.service;

import org.apache.commons.lang3.StringUtils;
import org.fmemetaj.warehousemanagment.entity.RegistrationForm;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Transactional
    public User createUser(RegistrationForm registrationForm, PasswordEncoder passwordEncoder) {
        var existingUser = userRepository.findByUsernameIgnoreCase(registrationForm.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Already registered");
        }

        if (StringUtils.isAnyBlank(registrationForm.getUsername(), registrationForm.getPassword())) {
            throw new IllegalStateException("Username and Password fields are required");
        }

        var user = new User()
                .setUsername(registrationForm.getUsername())
                .setPassword(passwordEncoder.encode(registrationForm.getPassword()))
                .setRole(registrationForm.getRole());

        return userRepository.save(user);
    }

}
