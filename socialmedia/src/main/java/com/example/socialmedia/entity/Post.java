package com.example.socialmedia.entity;

import jakarta.persistence.*;

@Entity
@Table(name="posts")
public class Post {
    
    @Id
    @Column(name = "postId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private Long userId;

    @Lob
    private String content;

    public Post() {

    }

    public Post(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public Post(String content) {
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
