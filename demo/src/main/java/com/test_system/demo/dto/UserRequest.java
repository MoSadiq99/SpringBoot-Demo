package com.test_system.demo.dto;

import com.test_system.demo.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {
    private User user;
    private Set<Long> roleIds;
}
