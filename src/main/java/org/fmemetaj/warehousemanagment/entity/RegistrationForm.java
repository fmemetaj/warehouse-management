package org.fmemetaj.warehousemanagment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationForm {
    private String username;
    private String password;
    private User.Role role;
}
