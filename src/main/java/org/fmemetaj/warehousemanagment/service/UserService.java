package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.controller.PasswordResetController;
import org.fmemetaj.warehousemanagment.entity.RegistrationForm;
import org.fmemetaj.warehousemanagment.entity.ResetPasswordResponse;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> findByUsername(String username);

    User createUser(RegistrationForm registrationForm);

    Result<User> updateUser(User user);

    boolean deleteUser(String username);

    ResetPasswordResponse requestNewPasswordByCurrentUser(User user, PasswordResetController.UpdatePasswordRequest updatedPasswordRequest);

    ResetPasswordResponse requestNewPassword(String username);
}
