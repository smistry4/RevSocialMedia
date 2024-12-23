package com.example.socialmedia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.socialmedia.entity.Post;
import com.example.socialmedia.entity.PostComment;
import com.example.socialmedia.entity.PostLike;
import com.example.socialmedia.entity.PostLikeId;
import com.example.socialmedia.repository.PostCommentRepository;
import com.example.socialmedia.repository.PostLikeRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.RelationshipRepository;

@Service
public class PostService {
    
    private PostRepository postRepository;

    private PostLikeRepository postLikeRepository;

    private PostCommentRepository postCommentRepository;

    private RelationshipRepository relationshipRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostLikeRepository postLikeRepository, PostCommentRepository postCommentRepository, RelationshipRepository relationshipRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
        this.relationshipRepository = relationshipRepository;
    }

    public Post createPost(Long userId, Post post) {
        post.setUserId(userId);
        return postRepository.save(post);
    }

    public Map<String, Object> getLikesForPost(Long userId, Long postId) {
        long count = postLikeRepository.countByPostId(postId);
        boolean likedByCurrentUser = postLikeRepository.existsByPostIdAndUserId(postId, userId);
    
        Map<String, Object> rv = new HashMap<>();
        rv.put("count", count);
        rv.put("likedByCurrentUser", likedByCurrentUser);
        return rv;
    }

    public void likePost(Long userId, Long postId) {
        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            PostLike pl = new PostLike(postId, userId);
            postLikeRepository.save(pl);
        }
    }

    public void unlikePost(Long userId, Long postId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            PostLikeId plid = new PostLikeId(postId, userId);
            postLikeRepository.deleteById(plid);
        }
    }

    public PostComment commentOnPost(Long userId, Long postId, PostComment postComment) {
        postComment.setUserId(userId);
        postComment.setPostId(postId);
        return postCommentRepository.save(postComment);
    }

    public List<PostComment> getComments(Long postId) {
        return postCommentRepository.findByPostId(postId).orElseThrow();
    }

    public List<Post> getUserFeed(Long userId) {
        List<Long> followedPeopleIds = relationshipRepository.findFollowedIdsByFollowerId(userId).orElseThrow();
        return postRepository.findByUserIdInOrderByPostIdDesc(followedPeopleIds).orElseThrow();
    }
}
