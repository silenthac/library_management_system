package com.prashant.library.library_management.controller;


import com.prashant.library.library_management.dto.UserRequestDTO;
import com.prashant.library.library_management.dto.UserResponseDTO;
import com.prashant.library.library_management.entity.User;
import com.prashant.library.library_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO request)
    {
        UserResponseDTO savedUser = userService.registerUser(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);


    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id)
    {
        UserResponseDTO dto = userService.getUserById(id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers()
    {
        List<UserResponseDTO> l = userService.getAllUsers();
        return new ResponseEntity<List<UserResponseDTO>>(l,HttpStatus.OK);
    }



}
