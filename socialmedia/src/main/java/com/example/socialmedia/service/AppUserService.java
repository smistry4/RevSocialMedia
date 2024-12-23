package com.example.socialmedia.service;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.socialmedia.entity.AppUsers;
import com.example.socialmedia.entity.Relationship;
import com.example.socialmedia.entity.RelationshipId;
import com.example.socialmedia.exception.ClientErrorException;
import com.example.socialmedia.exception.ResourceAlreadyExistsException;
import com.example.socialmedia.repository.AppUserRepository;
import com.example.socialmedia.repository.RelationshipRepository;
import com.example.socialmedia.security.JwtUtil;

@Service
public class AppUserService {
    
    private AppUserRepository appUserRepository;

    private RelationshipRepository relationshipRepository;

    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, RelationshipRepository relationshipRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.relationshipRepository = relationshipRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String registerUser(AppUsers appUser) throws ResourceAlreadyExistsException, ClientErrorException {
        Optional<AppUsers> optionalAppUser = appUserRepository.findByUsername(appUser.getUsername());
        Optional<AppUsers> emailUser = appUserRepository.findByEmail(appUser.getEmail());
        if (optionalAppUser.isPresent()) {
            throw new ResourceAlreadyExistsException("Account with username " + appUser.getUsername() + " already exists");
        }
        if (emailUser.isPresent()) {
            throw new ResourceAlreadyExistsException("Account with email " + appUser.getEmail() + " already exists");
        }
        else {
            appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
            appUserRepository.save(appUser);
        }
        return jwtUtil.generateToken(appUser.getUsername());
    }

    public String loginUser(AppUsers appUser) throws AuthenticationException {
        if (appUser.getUsername() == null || appUser.getUsername().isEmpty() || 
            appUser.getPassword() == null || appUser.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Username or password must not be empty");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
            return jwtUtil.generateToken(appUser.getUsername());
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public String updateUser(AppUsers updatedUser) throws ResourceAlreadyExistsException, ClientErrorException {
        AppUsers user = appUserRepository.findById(updatedUser.getUserId()).orElseThrow(() -> new ClientErrorException("Account does not exist"));

        if (!(updatedUser.getUsername().equals(user.getUsername()))) {
            Optional<AppUsers> optionalAppUser = appUserRepository.findByUsername(updatedUser.getUsername());
            if (optionalAppUser.isPresent()) {
                throw new ResourceAlreadyExistsException("Account with username " + updatedUser.getUsername() + " already exists");
            }
        }
        if (!(updatedUser.getEmail().equals(user.getEmail()))) {
            Optional<AppUsers> emailUser = appUserRepository.findByEmail(updatedUser.getEmail());
            if (emailUser.isPresent()) {
                throw new ResourceAlreadyExistsException("Account with email " + updatedUser.getEmail() + " already exists");
            }
        }
        if (updatedUser.getPassword().length() != 0) {
            updatedUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        } else {
            updatedUser.setPassword(user.getPassword());
        }
        appUserRepository.save(updatedUser);

        return jwtUtil.generateToken(updatedUser.getUsername());
    }

    public AppUsers getUserById(Long userId) {
        return appUserRepository.findById(userId).orElseThrow(()-> new ClientErrorException("Account does not exist"));
    }

    public AppUsers getUserByUsername(String username) {
        return appUserRepository.findByUsername(username).orElseThrow(() -> new ClientErrorException("Account does not exist"));
    }

    public List<AppUsers> searchUser(String query) throws ClientErrorException {
        if (query.length() == 0) {
            throw new ClientErrorException("Empty query");
        }
        return appUserRepository.searchByNameOrUsername(query);
    }

    public void followUser(Long userId, Long followedId) {
        if (userId == followedId) {
            throw new IllegalArgumentException("Cannot have same ids");
        }
        Relationship relationship = new Relationship(userId, followedId);
        relationshipRepository.save(relationship);
    }

    public void unfollowUser(Long userId, Long followedId) {
        if (userId == followedId) {
            throw new IllegalArgumentException("Cannot have same ids");
        }
        RelationshipId relationshipId = new RelationshipId(userId, followedId);
        relationshipRepository.deleteById(relationshipId);
    }

    public List<Long> getFollowingIds(Long userId) {
        return relationshipRepository.findFollowedIdsByFollowerId(userId).orElseThrow();
    }

    public List<Long> getFollowerIds(Long userId) {
        return relationshipRepository.findFollowerIdsByUserId(userId).orElseThrow();
    }

    public Boolean getFollowStatus(Long userId, Long otherId) {
        RelationshipId relationshipId = new RelationshipId(userId, otherId);
        return relationshipRepository.existsById(relationshipId);
    }
}
