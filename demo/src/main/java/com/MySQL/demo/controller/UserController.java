package com.MySQL.demo.controller;

import com.MySQL.demo.entity.User;
import com.MySQL.demo.exception.CustomException;
import com.MySQL.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //Todo: add validation
    @PostMapping("/users")
    public String postUser(@RequestBody User user) {
        userService.saveUser(user);
        return "User added successfully";
    }


    @PostMapping("/users-test")
    public String postUser(@RequestBody HashMap<String, String> temp) {
        //userService.saveUser(user);
        return "User added successfully";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

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

    @GetMapping("/users/paged")
    public Page<User> getUsersPagedAndSearched(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false, defaultValue = "") String search) {
        return userService.getUsersPagedAndSearched(page, perPage, search.trim());
    }

}
