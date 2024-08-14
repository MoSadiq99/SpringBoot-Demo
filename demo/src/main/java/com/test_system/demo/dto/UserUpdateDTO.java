package com.test_system.demo.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String passwordHash;
}
