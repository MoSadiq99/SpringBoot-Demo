package com.test_system.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String name;

    public Long getId() {
        return roleId;
    }

    //    @ManyToMany(mappedBy = "roles")
//    @JsonIgnore // Annotation to avoid infinite recursion
//    private Set<User> users = new HashSet<>();

    //? You can annotate the users field in the Role class with @JsonIgnore to prevent it from being serialized into JSON
}
