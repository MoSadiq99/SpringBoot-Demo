package com.test_system.demo.controller;

import com.test_system.demo.dto.UserRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class LoginController {

    //? Compare password and bcrypt, explore spring jwt token

//    @GetMapping("/login")
//    public String login( Authentication authentication) {
//        // Check if the user is authenticated
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            // Use userDetails to get username, roles, etc.
//            System.out.println("User logged in: " + userDetails.getUsername());
//        }
//        return "login"; // Return the login view
//    }

//    @GetMapping("/login")
//    public String login() {
//        return "login"; // Return the login view
//    }
//
//    @GetMapping("/home")
//    public String home(Authentication authentication) {
//        // Check if the user is authenticated
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            // Use userDetails to get username, roles, etc.
//            System.out.println("User logged in: " + userDetails.getUsername());
//        }
//        return "home"; // Return home view
//    }
//
//    @RequestMapping("/logout-success")
//    public String logoutSuccess() {
//        return "redirect:/login?logout"; // Redirect to login page after logout
//    }
}


//? Theory about Hybernate with spring security