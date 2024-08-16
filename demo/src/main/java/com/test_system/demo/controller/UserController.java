package com.test_system.demo.controller;

import com.test_system.demo.dto.LoginRequest;
import com.test_system.demo.dto.UserRequest;
import com.test_system.demo.entity.User;
import com.test_system.demo.exception.CustomException;
import com.test_system.demo.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
public class UserController {
    @Value("${upload.path}") // Folder to store uploaded user profile images
    private String uploadPath;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //* Creates a new user with the provided user data.
    @PostMapping("/users")
    public String postUser(@RequestBody User user) {
        userService.saveUser(user);
        return "User added successfully";
    }

    //* Creates a new user with the provided user data and role IDs.
    @PostMapping("/users/roles")
    public String postUser(@RequestBody @NotNull UserRequest userRequest) {
        userService.saveUser(userRequest.getUser(), userRequest.getRoleIds());
        return "User added successfully";
    }

    //* Assigns roles to a user with the provided user ID and role IDs.
    @PatchMapping("/users/{id}/roles")
    public ResponseEntity<User> assignRolesToUser(@PathVariable int id, @RequestBody Set<Long> roleIds) {
        try {
            User updatedUser = userService.assignRolesToUser(id, roleIds);
            return ResponseEntity.ok(updatedUser);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(null); // Or handle the error response differently
        }
    }

    //* Gets a list of users with pagination and search options.
    @GetMapping("/users/paged")
    public Page<User> getUsersPagedAndSearched(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false, defaultValue = "") String search) {
        return userService.getUsersPagedAndSearched(page, perPage, search.trim());
    }

    //* Gets a list of users.
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    //* Gets a list of users with roles.
    @GetMapping("/users/roles")
    public List<User> getAllUsersWithRoles() {
        return userService.getAllUsersWithRoles();
    }

    //* Gets a user with the provided user ID.
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    //* Patches a user with the provided user ID and user data.
    @PatchMapping("/users/{id}")
    public ResponseEntity<User> patchUser(@PathVariable int id, @RequestBody User userUpdates) {
        try {
            User updatedUser = userService.patchUser(id, userUpdates);
            return ResponseEntity.ok(updatedUser);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(null); // Or handle the error response differently
        }
    }

    //* Deletes a user with the provided user ID.
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    //* Logs in a user with the provided username and password.
    @PostMapping("/login")
    public String login(@RequestBody @NotNull LoginRequest userLogin) {
        return userService.login(userLogin.getUsername(), userLogin.getPassword());
    }

    //! Testing
    @PostMapping("/login/params")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userService.login(username, password);
    }

    //* Uploads a profile image for a user with the provided user ID.
    @PostMapping("/users/{id}/profile-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable int id, @RequestParam("file") @NotNull MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            // Ensure the upload directory exists
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file to the server
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, fileName);
            Files.copy(file.getInputStream(), filePath);

            // Save the file path in the database
            String filePathString = filePath.toString();
            userService.updateUserProfileImage(id, filePathString);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    //* Gets the profile image for a user with the provided user ID.
    @GetMapping("/users/{id}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable int id) {
        try {
            String imagePath = userService.getProfileImagePath(id);
            File imageFile = new File(imagePath);

            if (!imageFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] image = Files.readAllBytes(imageFile.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
