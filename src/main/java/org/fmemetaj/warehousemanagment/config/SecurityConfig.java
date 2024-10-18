package org.fmemetaj.warehousemanagment.config;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.security.JwtFilter;
import org.fmemetaj.warehousemanagment.security.WarehouseAuthenticationManager;
import org.fmemetaj.warehousemanagment.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UserServiceImpl userServiceImpl,
            PasswordEncoder passwordEncoder
    ) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/login"
                ).authenticated())
                .httpBasic(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                "/signup",
                "/password-reset/forgot-password",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui.html"
        ).permitAll());

        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .authenticationManager(new WarehouseAuthenticationManager(userServiceImpl, passwordEncoder))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserServiceImpl userServiceImpl,
            PasswordEncoder passwordEncoder
    ) {
        return new WarehouseAuthenticationManager(userServiceImpl, passwordEncoder);
    }
}
