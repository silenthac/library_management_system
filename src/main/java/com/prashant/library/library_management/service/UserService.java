package com.prashant.library.library_management.service;

import com.prashant.library.library_management.dto.UserRequestDTO;
import com.prashant.library.library_management.dto.UserResponseDTO;
import com.prashant.library.library_management.entity.User;
import com.prashant.library.library_management.exceptions.DuplicateEmailException;
import com.prashant.library.library_management.exceptions.ResourceNotFoundException;
import com.prashant.library.library_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {

        if (userRepository.findByEmail(requestDTO.getEmail()) != null) {
            throw new DuplicateEmailException("user already exists with email : " + requestDTO.getEmail());
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setFullName(requestDTO.getName());
        user.setRole(requestDTO.getRole());
        user.setRegistrationDate(LocalDate.now());
        user.setActive(true);
        user.setPassword(requestDTO.getPassword());

        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getRole(),
                saved.getRegistrationDate()
        );
    }


    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Direct mapping to DTO (no mapper class)
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getRegistrationDate()
        );
    }

    public List<UserResponseDTO> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()|| users==null) {
            throw new ResourceNotFoundException("No users found.");
        }
        return  users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public UserResponseDTO convertToDTO(User user)
    {
        UserResponseDTO dto = new UserResponseDTO( user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getRegistrationDate());

        return dto;

    }

}
