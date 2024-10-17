package org.fmemetaj.warehousemanagment.controller;

import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping("{username}")
    public ResponseEntity<User> findUserByUsername(
            @PathVariable String username
    ) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping("update")
    public ResponseEntity<Result<User>> updateUser(
            @RequestBody User user
    ) {
        return Result.response(userService.updateUser(user));
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping("{username}/delete")
    public ResponseEntity<?> deleteUserByUsername(
            @PathVariable String username
    ) {
        return userService.deleteUser(username)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be deleted");
    }
}
