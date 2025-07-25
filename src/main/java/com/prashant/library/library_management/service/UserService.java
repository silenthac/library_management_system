package com.prashant.library.library_management.service;

import com.prashant.library.library_management.dto.UserPatchRequestDTO;
import com.prashant.library.library_management.dto.UserRequestDTO;
import com.prashant.library.library_management.dto.UserResponseDTO;
import com.prashant.library.library_management.entity.Role;
import com.prashant.library.library_management.entity.User;
import com.prashant.library.library_management.exceptions.DuplicateEmailException;
import com.prashant.library.library_management.exceptions.ResourceNotFoundException;
import com.prashant.library.library_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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


    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO)
    {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        User existingUser = userRepository.findByEmail(requestDTO.getEmail());

        if (!user.getEmail().equalsIgnoreCase(requestDTO.getEmail())) {
            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                throw new DuplicateEmailException("User already exists with email: " + requestDTO.getEmail());
            }
            user.setEmail(requestDTO.getEmail());
        }
        user.setFullName(requestDTO.getName());
        user.setRole(requestDTO.getRole());
        user.setActive(true);


        userRepository.save(user);

        return convertToDTO(user);


    }

    public UserResponseDTO patchUser(Long id, UserPatchRequestDTO request)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(request.getName()!=null)
        {
            user.setFullName(request.getName());
        }
        if(request.getEmail()!=null)
        {
            User existingUser = userRepository.findByEmail(request.getEmail());

            if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
                if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                    throw new DuplicateEmailException("User already exists with email: " + request.getEmail());
                }
                user.setEmail(request.getEmail());
            }

        }

        if(request.getRole()!=null)
        {
            user.setRole(request.getRole());
        }

        userRepository.save(user);


        UserResponseDTO dto = convertToDTO(user);

        return dto;


    }

    public void deleteUser(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("user Not found with the id:" + id));

        userRepository.deleteById(id);
    }

    public UserResponseDTO getUserByEmail(String email)
    {
        User user = userRepository.findByEmail(email);
        if(user==null)
        {
            throw new ResourceNotFoundException("User is not present with this email");
        }

        return convertToDTO(user);


    }

    public UserResponseDTO getUserByRole(Role role)
    {
        User user = userRepository.findByRole(role);
        if(user==null)
        {
            throw new ResourceNotFoundException("User is not present with this role");
        }

        return convertToDTO(user);


    }


    public List<UserResponseDTO> getUsersByName(String name)
    {
        // Use lowercase for method params (Java convention)
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);

        // Null check not needed if repository never returns null
        if (users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("No users found with name: " + name);
        }

        // Convert to DTOs and return
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<UserResponseDTO> getUsersPaginated(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(this::convertToDTO);
    }




}
