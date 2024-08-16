package com.test_system.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    private String username; // Required and unique

    @Column(name = "fullName", nullable = false)
    @NotBlank(message = "Full name is required")
    private String fullName; // Required

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email; // Required, email format validation, unique

    @Column(name = "phoneNumber")
    @Size(max = 10, message = "Phone number should be up to 10 digits")
    private String phoneNumber; // Optional

    @Column(name = "passwordHash", nullable = false)
    @NotBlank(message = "Password is required")
    private String passwordHash; // Required, encrypt the password

    @Column(name = "profileImage")
    private String profileImage; // Store the path to the profile image

//    @Lob
//    @Column(name = "profileImage")
//    private byte[] profileImage; // Store the image as binary data

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
