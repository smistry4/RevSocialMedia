package com.example.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.socialmedia.entity.Relationship;
import com.example.socialmedia.entity.RelationshipId;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId>{

    @Query("SELECT r.followedId FROM Relationship r WHERE r.followerId = :followerId")
    Optional<List<Long>> findFollowedIdsByFollowerId(@Param("followerId") Long followerId);

    @Query("SELECT r.followerId FROM Relationship r WHERE r.followedId = :userId")
    Optional<List<Long>> findFollowerIdsByUserId(@Param("userId") Long userId);
}
