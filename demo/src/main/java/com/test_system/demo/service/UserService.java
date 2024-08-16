package com.test_system.demo.service;

import com.test_system.demo.entity.Role;
import com.test_system.demo.entity.User;
import com.test_system.demo.exception.CustomException;
import com.test_system.demo.repository.RoleRepository;
import com.test_system.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private RoleRepository roleRepository; // Add a RoleRepository to interact with the Role entity

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // * Creates a new user with the provided user data.
    public User saveUser(User user) {
        try {
            user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate entry scenario
            throw new CustomException("A user with this email or username already exists.");
        }
    }

    // * Creates a new user with the provided user data and role IDs.
    public User saveUser(User user, Set<Long> roleIds) {
        // Check if email or username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("A user with this email already exists.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("A user with this username already exists.");
        }

        try {
            user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));

            // Fetch roles from the RoleRepository
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new CustomException(STR."A user with this email or username already exists.\{HttpStatus.BAD_REQUEST}");

        } catch (DataIntegrityViolationException e) {
            throw new CustomException(STR."Validation failed: \{e.getMessage()}");

        } catch (Exception e) {
            throw new CustomException("An error occurred while saving the user.");
        }
    }

    // * Assigns roles to a user with the provided user ID and role IDs.
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

    // * Gets a list of users.
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // * Gets a user with the provided user ID.
    @Transactional
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);

    }

    //! Method to get all users with their roles - User Created - But Default Method findAll() is same as findAllWithRoles()
    @Transactional
    public List<User> getAllUsersWithRoles() {
        return userRepository.findAllWithRoles();
    }

    //* Patches a user with the provided user ID and user data.
    public User patchUser(int id, User userUpdates) {
        User existingUser = findExistingUser(id);
        updateUserFields(existingUser, userUpdates);
        updateUserRoles(existingUser, userUpdates);

        return saveUser(existingUser);
    }
    private User findExistingUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found."));
    }

    private void updateUserFields(User existingUser, User userUpdates) {
        Optional.ofNullable(userUpdates.getUsername()).ifPresent(existingUser::setUsername);
        Optional.ofNullable(userUpdates.getFullName()).ifPresent(existingUser::setFullName);
        Optional.ofNullable(userUpdates.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(userUpdates.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);

        // Update password only if provided and not null
        if (userUpdates.getPasswordHash() != null) {
            existingUser.setPasswordHash(userUpdates.getPasswordHash());
        }
    }

    private void updateUserRoles(User existingUser, User userUpdates) {
        if (userUpdates.getRoles() != null) {
            Set<Long> roleIds = userUpdates.getRoles().stream()
                    .map(Role::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!roleIds.isEmpty()) {
                Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
                if (roles.size() != roleIds.size()) {
                    throw new CustomException("Some roles were not found.");
                }
                existingUser.setRoles(roles);
            }
        }
    }

    //* Gets a list of users with pagination and search options.
    public Page<User> getUsersPagedAndSearched(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page - 1, size); // Adjust if needed
        return userRepository.findBySearch(search, pageable);
    }

    //* Deletes a user with the provided user ID.
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found.");
        }
        userRepository.deleteById(id);
    }

    //* Gets a user with the provided username.
    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPasswordHash())) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }

    //* Updates the profile image of a user with the provided user ID and image path.
    public void updateUserProfileImage(int userId, String imagePath) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfileImage(imagePath);
        userRepository.save(user);
    }

    //* Gets the profile image path of a user with the provided user ID.
    public String getProfileImagePath(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProfileImage();
    }
}
