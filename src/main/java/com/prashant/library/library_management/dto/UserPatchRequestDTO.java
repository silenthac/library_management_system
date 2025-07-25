package com.prashant.library.library_management.dto;

import com.prashant.library.library_management.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserPatchRequestDTO {

    private String name;


    @Email(message = "Invalid Email format")
    private String Email;

    private Role role;
    private String password;

    public String getEmail() {
        return this.Email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
