package com.bankapp.bankapp.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Registration
 * @author Fam Le Duc Nam
 */

@Profile("!test")
@Configuration
public class MySecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/main",
                                "/account",
                                "/transaction",
                                "/login",
                                "/home/homePage.css",
                                "/account/accounts.css",
                                "/transaction/account-transaction.css",
                                "/login/custom-login.css",
                                "/custom-login.css"
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults()) //t -> t.loginPage("/login")
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    /**
     * Registration Users
     * @username    nam
     * @password    password
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("nam")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}