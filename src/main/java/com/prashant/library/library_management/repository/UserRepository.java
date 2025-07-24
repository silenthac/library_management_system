package com.prashant.library.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prashant.library.library_management.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);


}
