package com.prashant.library.library_management.repository;

import com.prashant.library.library_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prashant.library.library_management.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByRole(Role role);
    List<User> findByNameContainingIgnoreCase(String name);


}
