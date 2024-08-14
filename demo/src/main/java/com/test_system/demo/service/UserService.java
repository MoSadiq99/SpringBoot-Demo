package com.test_system.demo.service;

import com.test_system.demo.entity.Role;
import com.test_system.demo.entity.User;
import com.test_system.demo.exception.CustomException;
import com.test_system.demo.repository.RoleRepository;
import com.test_system.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

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

    public User saveUser(User user, Set<Long> roleIds) {
        try {
            user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));

            // Fetch roles from the RoleRepository
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("A user with this email or username already exists.");

        } catch (ConstraintViolationException e) {
            throw new CustomException("Validation failed: " + e.getMessage());

        } catch (Exception e) {
            throw new CustomException("An error occurred while saving the user.");
        }

    }

    @Autowired
    private RoleRepository roleRepository; // Add a RoleRepository to interact with the Role entity

    public User assignRolesToUser(int userId, Set<Long> roleIds) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Fetch roles from the RoleRepository
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            existingUser.setRoles(roles);

            return userRepository.save(existingUser);
        } else {
            throw new CustomException("User not found.");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
//        user.getrole()
    }
    //!Have to implement role based access
//    @Transactional
//    public User getUserById(int id) {
//        return userRepository.findByIdWithRoles(id).orElse(null);
//    }

    @Transactional
    public List<User> getAllUsersWithRoles() {
        return userRepository.findAllWithRoles();
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
                existingUser.setPasswordHash(bCryptPasswordEncoder.encode(userUpdates.getPasswordHash()));
                //existingUser.setPasswordHash(userUpdates.getPasswordHash());
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

    public Page<User> getUsersPagedAndSearched(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page - 1, size); // Adjust if needed
        return userRepository.findBySearch(search, pageable);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
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
}
