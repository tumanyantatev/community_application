package com.application.community.repositories;

import com.application.community.models.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    @Query("SELECT t FROM token t WHERE t.token = :token")
    JwtToken findByToken(@Param("token") String token);
}
