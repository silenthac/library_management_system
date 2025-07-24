package com.prashant.library.library_management.dto;

import com.prashant.library.library_management.entity.Role;

import java.time.LocalDate;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDate registrationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserResponseDTO(Long id, String name, String email, Role role, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.registrationDate = registrationDate;
    }
}
