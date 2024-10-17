package org.fmemetaj.warehousemanagment.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.service.UserService;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private final UserService userService;
    private final KeyPair keyPair;
    private final JwtParser jwtParser;

    public JwtService(UserService userService) {
        this.userService = userService;
        this.keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build();
    }

    public String createJwtToken(User user) {
        var timestamp = Instant.now();

        return Jwts.builder()
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.ES256)
                .setSubject(StringUtils.lowerCase(user.getUsername()))
                .setIssuer("FMEMETAJ")
                .claim("accessLevel", user.getRole())
                .setExpiration(Date.from(timestamp.plus(1, ChronoUnit.DAYS)))
                .setIssuedAt(Date.from(timestamp))
                .compact();
    }

    public Optional<User> getUserFromJwt(String jwt) {
        var username = jwtParser.parseClaimsJws(jwt)
                .getBody()
                .getSubject();

        return Optional.ofNullable(username)
                .flatMap(userService::findByUsername);
    }
}
