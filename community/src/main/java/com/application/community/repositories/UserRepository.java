package com.application.community.repositories;

import com.application.community.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM users u WHERE u.firstName like %:name% or u.lastName like %:name%")
    List<User> find(@Param("name") String searchString, Pageable page);

    @Query("SELECT u FROM users u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);
}
