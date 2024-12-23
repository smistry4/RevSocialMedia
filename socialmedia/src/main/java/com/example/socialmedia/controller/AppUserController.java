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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialmedia.entity.AppUsers;
import com.example.socialmedia.service.AppUserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class AppUserController {

    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    private Long getUserId(Authentication authentication) {
        String username = authentication.getName();
        return appUserService.getUserByUsername(username).getUserId();
    }

    @GetMapping("/current")
    public ResponseEntity<AppUsers> getCurrentUser(Authentication authentication) {
        AppUsers user = appUserService.getUserByUsername(authentication.getName());
        return ResponseEntity.status(200).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUsers> getUserById(@PathVariable Long userId) {
        AppUsers retrievedAccount = appUserService.getUserById(userId);
        return ResponseEntity.status(200).body(retrievedAccount);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppUsers>> searchUser(@RequestParam String username) {
        List<AppUsers> user = appUserService.searchUser(username);
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping("/{followedId}/follow")
    public ResponseEntity<String> followUser(Authentication authentication, @PathVariable Long followedId) {
        Long userId = getUserId(authentication);
        appUserService.followUser(userId, followedId);
        return ResponseEntity.status(200).body("Follow Successful");
    }

    @DeleteMapping("/{followedId}/follow")
    public ResponseEntity<String> unfollowUser(Authentication authentication, @PathVariable Long followedId) {
        Long userId = getUserId(authentication);
        appUserService.unfollowUser(userId, followedId);
        return ResponseEntity.status(200).body("Unfollowed successfully");
    }

    @GetMapping("/followers")
    public ResponseEntity<List<Long>> getFollowers(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<Long> followers = appUserService.getFollowerIds(userId);
        return ResponseEntity.status(200).body(followers);
    }

    @GetMapping("/following")
    public ResponseEntity<List<Long>> getFollowing(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<Long> following = appUserService.getFollowingIds(userId);
        return ResponseEntity.status(200).body(following);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(Authentication authentication, @RequestBody AppUsers appUsers) {
        String token = appUserService.updateUser(appUsers);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("Update successful");
    }

    @GetMapping("/{otherId}/relation")
    public ResponseEntity<Map<String, Boolean>> isFollowing(Authentication authentication, @PathVariable Long otherId) {
        Long userId = getUserId(authentication);
        Boolean isRelated = appUserService.getFollowStatus(userId, otherId);
        Map<String, Boolean> rv = Map.of("following", isRelated);
        return ResponseEntity.status(200).body(rv);
    }

    /*
     * get a user{/{id}}
     * login,authenticate {login}
     * update user info {/{id}}
     * delete user {/{id}}
     */
}
