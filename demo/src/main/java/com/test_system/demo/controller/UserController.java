package com.test_system.demo.controller;

import com.test_system.demo.dto.UserRequest;
import com.test_system.demo.entity.User;
import com.test_system.demo.exception.CustomException;
import com.test_system.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
public class UserController {
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
    public String postUser(@RequestBody UserRequest userRequest) {
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

    //* Deletes a user with the provided user ID.
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User deleted successfully";
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
//    @PatchMapping("/users/{id}/profile-image")
//    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
//        try {
//            userService.updateProfileImage(id, file);
//            return ResponseEntity.ok("Image uploaded successfully.");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
//        } catch (CustomException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

//    @GetMapping("/users/{id}/profile-image")
//    public ResponseEntity<byte[]> getProfileImage(@PathVariable int id) {
//        try {
//            byte[] image = userService.getProfileImage(id);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG) // Change to the correct media type based on your image format
//                    .body(image);
//        } catch (CustomException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
}
