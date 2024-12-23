package com.example.socialmedia.entity;

import java.io.Serializable;
import java.util.Objects;


public class RelationshipId implements Serializable{
    private Long followerId;
    private Long followedId;

    public RelationshipId() {

    }

    public RelationshipId(Long followerId, Long followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelationshipId r = (RelationshipId) o;
        return followerId == r.followerId && followedId == r.followedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followedId);
    }
}
