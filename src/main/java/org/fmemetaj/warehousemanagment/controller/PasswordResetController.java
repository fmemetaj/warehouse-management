package org.fmemetaj.warehousemanagment.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fmemetaj.warehousemanagment.entity.ResetPasswordResponse;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final UserService userService;

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("current-user")
    public ResponseEntity<ResetPasswordResponse> resetPasswordFromCurrentUser(
            @AuthenticationPrincipal User user,
            @RequestBody UpdatePasswordRequest updatePasswordRequest
    ) {
        var resetPasswordResponse = userService.requestNewPasswordByCurrentUser(user, updatePasswordRequest);
        return resetPasswordResponse.isSuccess()
                ? ResponseEntity.ok(resetPasswordResponse)
                : ResponseEntity.badRequest().body(resetPasswordResponse);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) {
        var resetPasswordResponse = userService.requestNewPassword(forgotPasswordRequest.getUsername());
        return resetPasswordResponse.isSuccess()
                ? ResponseEntity.ok(resetPasswordResponse)
                : ResponseEntity.badRequest().body(resetPasswordResponse);
    }


    @Data
    @NoArgsConstructor
    public static final class ForgotPasswordRequest {
        private String username;
    }

    @Data
    @NoArgsConstructor
    public static final class UpdatePasswordRequest {
        private String password;
        private String passwordConfirmation;

        public boolean isValid() {
            return StringUtils.equals(password, passwordConfirmation);
        }
    }
}
