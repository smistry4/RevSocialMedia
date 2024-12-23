package com.example.socialmedia.entity;

import java.io.Serializable;
import java.util.Objects;

public class PostLikeId implements Serializable{
    private Long postId;
    private Long userId;

    public PostLikeId() {

    }

    public PostLikeId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostLikeId plid = (PostLikeId)o;
        return postId == plid.postId && userId == plid.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
