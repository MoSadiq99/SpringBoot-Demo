package com.MySQL.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Return the login view
    }

    @GetMapping("/home")
    public String home(Authentication authentication) {
        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Use userDetails to get username, roles, etc.
            System.out.println("User logged in: " + userDetails.getUsername());
        }
        return "home"; // Return home view
    }

    @RequestMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/login?logout"; // Redirect to login page after logout
    }
}
