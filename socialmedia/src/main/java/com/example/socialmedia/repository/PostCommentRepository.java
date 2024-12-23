package com.example.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.socialmedia.entity.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>{
    Optional<List<PostComment>>findByPostId(Long postId);
}
