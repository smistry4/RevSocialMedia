package com.example.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.socialmedia.entity.PostLike;
import com.example.socialmedia.entity.PostLikeId;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId>{
    Long countByPostId(Long postId);
    Boolean existsByPostIdAndUserId(Long postId, Long userId);
}
