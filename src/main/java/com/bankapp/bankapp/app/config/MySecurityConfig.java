package com.bankapp.bankapp.app.config;

import com.bankapp.bankapp.app.config.SecurityRegistration.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
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
 *
 * @author Fam Le Duc Nam
 */
@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class MySecurityConfig {

//    private final JwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/main",
                                "/account",
                                "/transaction",
                                "/transfer",
                                "/aboutMe",
                                "/bootstrap",
                                "/reset",
                                "/login",
                                "/register",
                                "/css/bootstrap.min.css",
                                "/js/bootstrap.min.js"
                        )
                        .permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults())//t -> t.loginPage("/login"))
//                .logout(LogoutConfigurer::permitAll)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationFilter);
//        return authenticationManagerBuilder.build();
//    }

    /**
     * Registration Users
     * Username:     nam
     * Password:   user123
     * ====================
     * Registration Admin
     * AdminName:   admin
     * Password:   admin123
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("nam")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}