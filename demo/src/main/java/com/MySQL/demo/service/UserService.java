package com.MySQL.demo.service;

import com.MySQL.demo.entity.User;
import com.MySQL.demo.exception.CustomException;
import com.MySQL.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

//    @Autowired
//    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder; //! Implemented BCryptPasswordEncoder instead of PasswordEncoder


    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        try {
            user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate entry scenario
            throw new CustomException("A user with this email or username already exists.");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);

    }

    public User patchUser(int id, User userUpdates) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Update fields based on provided data
            if (userUpdates.getUsername() != null) {
                existingUser.setUsername(userUpdates.getUsername());
            }
            if (userUpdates.getFullName() != null) {
                existingUser.setFullName(userUpdates.getFullName());
            }
            if (userUpdates.getEmail() != null) {
                existingUser.setEmail(userUpdates.getEmail());
            }
            if (userUpdates.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(userUpdates.getPhoneNumber());
            }
            if (userUpdates.getPasswordHash() != null) {
                existingUser.setPasswordHash(userUpdates.getPasswordHash());
            }

            try {
                return userRepository.save(existingUser);
            } catch (DataIntegrityViolationException e) {
                throw new CustomException("A user with this email or username already exists.");
            }
        } else {
            throw new CustomException("User not found.");
        }
    }

//    public User updateProfileImage(int id, MultipartFile file) throws IOException {
//        Optional<User> existingUserOptional = userRepository.findById(id);
//        if (existingUserOptional.isPresent()) {
//            User existingUser = existingUserOptional.get();
//            existingUser.setProfileImage(file.getBytes());
//            return userRepository.save(existingUser);
//        } else {
//            throw new CustomException("User not found.");
//        }
//    }

//    public byte[] getProfileImage(int id) {
//        Optional<User> existingUserOptional = userRepository.findById(id);
//        if (existingUserOptional.isPresent()) {
//            return existingUserOptional.get().getProfileImage();
//        } else {
//            throw new CustomException("User not found.");
//        }
//    }

    public Page<User> getUsersPagedAndSearched(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page - 1, size); // Adjust if needed
        return userRepository.findBySearch(search, pageable);
    }

}
