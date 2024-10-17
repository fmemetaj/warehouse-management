package org.fmemetaj.warehousemanagment.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "fromJwt")
public final class Token {
    private String token;
}
