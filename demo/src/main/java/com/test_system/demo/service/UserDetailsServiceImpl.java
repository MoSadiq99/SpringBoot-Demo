package com.test_system.demo.service;

import com.test_system.demo.entity.User;
import com.test_system.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder; // Use UserBuilder for building UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    //* Load user by username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //? Check if password is plain text
        //! Implemented because I already stored the password in plain text
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
