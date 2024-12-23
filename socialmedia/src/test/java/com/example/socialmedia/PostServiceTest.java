package com.example.socialmedia;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.socialmedia.entity.Post;
import com.example.socialmedia.entity.PostComment;
import com.example.socialmedia.entity.PostLike;
import com.example.socialmedia.entity.PostLikeId;
import com.example.socialmedia.repository.PostCommentRepository;
import com.example.socialmedia.repository.PostLikeRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.RelationshipRepository;
import com.example.socialmedia.service.PostService;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    
    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostCommentRepository postCommentRepository;

    @Mock
    private RelationshipRepository relationshipRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testCreatePost_ShouldSavePost_WhenValidPost() {
        // Arrange
        Long userId = 1L;
        Post post = new Post();
        post.setContent("This is a new post");

        Post savedPost = new Post();
        savedPost.setUserId(userId);
        savedPost.setContent("This is a new post");

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        Post result = postService.createPost(userId, post);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("This is a new post", result.getContent());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testGetLikesForPost_ShouldReturnLikesAndLikeStatus() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;

        long likeCount = 10;
        boolean likedByCurrentUser = true;

        when(postLikeRepository.countByPostId(postId)).thenReturn(likeCount);
        when(postLikeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(likedByCurrentUser);

        // Act
        Map<String, Object> result = postService.getLikesForPost(userId, postId);

        // Assert
        assertEquals(likeCount, result.get("count"));
        assertTrue((Boolean) result.get("likedByCurrentUser"));
        verify(postLikeRepository, times(1)).countByPostId(postId);
        verify(postLikeRepository, times(1)).existsByPostIdAndUserId(postId, userId);
    }

    @Test
    public void testLikePost_ShouldSavePostLike_WhenUserHasNotLikedYet() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;

        when(postLikeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);

        // Act
        postService.likePost(userId, postId);

        // Assert
        verify(postLikeRepository, times(1)).save(any(PostLike.class));
    }

    @Test
    public void testLikePost_ShouldNotSavePostLike_WhenUserHasAlreadyLiked() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;

        when(postLikeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(true);

        // Act
        postService.likePost(userId, postId);

        // Assert
        verify(postLikeRepository, times(0)).save(any(PostLike.class));
    }

    @Test
    public void testUnlikePost_ShouldDeletePostLike_WhenExists() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;

        PostLikeId postLikeId = new PostLikeId(postId, userId);

        when(postLikeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(true);

        // Act
        postService.unlikePost(userId, postId);

        // Assert
        verify(postLikeRepository).existsByPostIdAndUserId(postId, userId);
        verify(postLikeRepository).deleteById(postLikeId);
    }

    @Test
    public void testUnlikePost_ShouldDoNothing_WhenLikeDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;

        when(postLikeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);

        // Act
        postService.unlikePost(userId, postId);

        // Assert
        verify(postLikeRepository, times(0)).deleteById(any(PostLikeId.class));
    }

    @Test
    public void testCommentOnPost_ShouldSaveComment_WhenValidComment() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;
        PostComment postComment = new PostComment();
        postComment.setContent("Great post!");

        PostComment savedComment = new PostComment();
        savedComment.setUserId(userId);
        savedComment.setPostId(postId);
        savedComment.setContent("Great post!");

        when(postCommentRepository.save(any(PostComment.class))).thenReturn(savedComment);

        // Act
        PostComment result = postService.commentOnPost(userId, postId, postComment);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(postId, result.getPostId());
        assertEquals("Great post!", result.getContent());
        verify(postCommentRepository).save(postComment);
    }

    @Test
    public void testGetComments_ShouldReturnListOfComments() {
        // Arrange
        Long postId = 2L;
        List<PostComment> comments = Arrays.asList(
            new PostComment(1L, postId, "Nice post!"),
            new PostComment(2L, postId, "I agree!")
        );

        when(postCommentRepository.findByPostId(postId)).thenReturn(Optional.of(comments));

        // Act
        List<PostComment> result = postService.getComments(postId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Nice post!", result.get(0).getContent());
        assertEquals("I agree!", result.get(1).getContent());
        verify(postCommentRepository).findByPostId(postId);
    }

    @Test
    public void testGetComments_ShouldThrowException_WhenNoCommentsExist() {
        // Arrange
        Long postId = 2L;

        when(postCommentRepository.findByPostId(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> postService.getComments(postId));
    }

    @Test
    public void testGetUserFeed_ShouldReturnPosts_WhenUserFollowsOthers() {
        // Arrange
        Long userId = 1L;
        List<Long> followedIds = Arrays.asList(2L, 3L);
        List<Post> posts = Arrays.asList(new Post(), new Post());

        when(relationshipRepository.findFollowedIdsByFollowerId(userId)).thenReturn(Optional.of(followedIds));
        when(postRepository.findByUserIdInOrderByPostIdDesc(followedIds)).thenReturn(Optional.of(posts));

        // Act
        List<Post> result = postService.getUserFeed(userId);

        // Assert
        assertEquals(2, result.size());
        verify(postRepository).findByUserIdInOrderByPostIdDesc(followedIds);
    }

    @Test
    public void testGetUserFeed_ShouldThrowException_WhenUserHasNoFollowers() {
        // Arrange
        Long userId = 1L;

        when(relationshipRepository.findFollowedIdsByFollowerId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> postService.getUserFeed(userId));
    }
}
