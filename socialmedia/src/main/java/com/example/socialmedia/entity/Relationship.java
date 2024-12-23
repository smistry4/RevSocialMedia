package com.example.socialmedia.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "relationships")
@IdClass(RelationshipId.class)
public class Relationship {

    @Id
    private Long followerId;
    
    @Id
    private Long followedId;

    public Relationship() {

    }

    public Relationship(Long followerId, Long followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFollowedId() {
        return followedId;
    }
}
