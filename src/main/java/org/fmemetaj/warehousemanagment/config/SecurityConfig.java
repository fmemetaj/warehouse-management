package org.fmemetaj.warehousemanagment.config;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.security.JwtFilter;
import org.fmemetaj.warehousemanagment.security.WarehouseAuthenticationManager;
import org.fmemetaj.warehousemanagment.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/login"
                ).authenticated())
                .httpBasic(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                "/signup"
        ).permitAll());

        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .authenticationManager(new WarehouseAuthenticationManager(userService, passwordEncoder))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        return new WarehouseAuthenticationManager(userService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
