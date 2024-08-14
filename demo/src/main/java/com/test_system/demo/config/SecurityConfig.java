package com.test_system.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
////                                .requestMatchers("/").permitAll()
//                                .requestMatchers("/login", "/register","/users" ,"/users/**").permitAll()
//                                .anyRequest().permitAll()
//
//                                //.anyRequest().authenticated()
////                                .requestMatchers("/users/**").authenticated() // Ensure /users is protected
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/home", true) // Redirect to /home after successful login
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout
//                                .logoutUrl("/logout")
//                                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
//                                .permitAll()
//                );
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(@org.jetbrains.annotations.NotNull HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/register", "/users", "/users/**").permitAll()
                                .anyRequest().authenticated() // Ensure other requests are authenticated
                ).formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/home", true) // Redirect to /home after successful login
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                                .permitAll()
                );
        return http.build();
    }


    @Bean  // Bean for password encoder optional
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
