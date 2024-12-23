package com.example.socialmedia.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.socialmedia.entity.AppUsers;

@Repository
public interface AppUserRepository extends JpaRepository<AppUsers, Long> {
    Optional<AppUsers> findByUsername(String username);
    Optional<AppUsers> findByEmail(String email);
    Optional<AppUsers> findByUsernameAndPassword(String username, String password);

    @Query("SELECT u FROM AppUsers u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%',:query,'%'))")
    List<AppUsers> searchByNameOrUsername(@Param("query") String query);
}
