package com.example.socialmedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmedia.entity.Post;
import com.example.socialmedia.entity.PostComment;
import com.example.socialmedia.service.AppUserService;
import com.example.socialmedia.service.PostService;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AppUserService appUserService;

    private Long getUserId(Authentication authentication) {
        String username = authentication.getName();
        return appUserService.getUserByUsername(username).getUserId();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(Authentication authentication, @RequestBody Post post) {
        Long userId = getUserId(authentication);
        Post createdPost = postService.createPost(userId, post);
        return ResponseEntity.status(200).body(createdPost);
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> getLikesForPost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserId(authentication);
        Map<String, Object> rv = postService.getLikesForPost(userId, postId);
        return ResponseEntity.status(200).body(rv);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserId(authentication);
        postService.likePost(userId, postId);
        return ResponseEntity.status(200).body("Liked");
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserId(authentication);
        postService.unlikePost(userId, postId);
        return ResponseEntity.status(200).body("Unliked");
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<PostComment> commentOnPost(Authentication authentication, @PathVariable Long postId, @RequestBody PostComment postComment) {
        Long userId = getUserId(authentication);
        PostComment ps = postService.commentOnPost(userId, postId, postComment);
        return ResponseEntity.status(200).body(ps);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<PostComment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.status(200).body(postService.getComments(postId));
    }
 
    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getUserFeed(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.status(200).body(postService.getUserFeed(userId));
    }
    
    /*
     * create a post
     * get/read a post {/{id}}
     * getallpostsbyuserid {/{userId}}
     * update a post only the one created by user.
     * delete a post only the one created by user.
     */
}
