package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.RegistrationForm;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User createUser(RegistrationForm registrationForm);

    Result<User> updateUser(User user);

    void deleteUser(String username);

    Result<User> findUserByUsername(String username);

    void requestNewPassword(String username);
}
