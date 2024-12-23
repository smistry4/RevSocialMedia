package com.example.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.socialmedia.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    Optional<List<Post>> findByUserIdInOrderByPostIdDesc(List<Long> userIds);
}
