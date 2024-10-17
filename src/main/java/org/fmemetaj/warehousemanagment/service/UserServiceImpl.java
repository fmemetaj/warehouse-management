package org.fmemetaj.warehousemanagment.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.fmemetaj.warehousemanagment.controller.PasswordResetController;
import org.fmemetaj.warehousemanagment.entity.RegistrationForm;
import org.fmemetaj.warehousemanagment.entity.ResetPasswordResponse;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional
    public User createUser(RegistrationForm registrationForm) {
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

    @Override
    @Transactional
    public Result<User> updateUser(User user) {
        var foundUserOpt = userRepository.findByUsernameIgnoreCase(user.getUsername());
        if (foundUserOpt.isEmpty()) {
            return Result.error(Result.Code.USER_NOT_FOUND);
        }

        var foundUser = foundUserOpt.get();
        Optional.of(user.getUsername()).ifPresent(foundUser::setUsername);
        Optional.of(user.getPassword()).ifPresent(password -> foundUser.setPassword(passwordEncoder.encode(password)));
        Optional.of(user.getRole()).ifPresent(foundUser::setRole);

        return Result.successful(userRepository.save(foundUser));
    }

    @Override
    @Transactional
    public boolean deleteUser(String username) {
        var foundUserOpt = userRepository.findByUsernameIgnoreCase(username);
        foundUserOpt.ifPresent(userRepository::delete);
        return foundUserOpt.isPresent();
    }

    @Override
    public ResetPasswordResponse requestNewPasswordByCurrentUser(User user, PasswordResetController.UpdatePasswordRequest updatePasswordRequest) {
        if (!updatePasswordRequest.isValid()) {
            return ResetPasswordResponse.error("Passwords did not match");
        }

        updateUserPassword(user, updatePasswordRequest.getPassword());

        return ResetPasswordResponse.success("Password was updated");
    }

    @Override
    @Transactional
    public ResetPasswordResponse requestNewPassword(String username) {

        var byUsername = userRepository.findByUsernameIgnoreCase(username);
        if (byUsername.isEmpty()) {
            return ResetPasswordResponse.error("Unknown username");
        }

        var user = byUsername.get();

        log.info("Overriding Password for User: {} ({})", user.getId(), user.getUsername());

        var temporaryPassword = String.valueOf(Hex.encodeHex(RandomUtils.nextBytes(32)));

        log.info("User {} new password: {}", user.getUsername(), temporaryPassword);

        updateUserPassword(user, temporaryPassword);

        return ResetPasswordResponse.success("Password was reset, and an email was sent with a temporary password");
    }

    private void updateUserPassword(User user, String newPassword) {
        var encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}
