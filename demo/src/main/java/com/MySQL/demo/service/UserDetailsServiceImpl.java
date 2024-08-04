package com.MySQL.demo.service;

import com.MySQL.demo.entity.User;
import com.MySQL.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder; // Use UserBuilder for building UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        com.MySQL.demo.entity.User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Build UserDetails with Spring Security's User class
//        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
//        builder.password(user.getPasswordHash());
//        builder.roles("USER");
//
//        return builder.build();
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if password is plain text
        if (!bCryptPasswordEncoder.matches(user.getPasswordHash(), user.getPasswordHash())) {
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPasswordHash());
            user.setPasswordHash(encodedPassword);
            userRepository.save(user);
        }

        // Build UserDetails with Spring Security's User class
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPasswordHash()); // Use the hashed password
        builder.roles("USER");

        return builder.build();
    }
}
