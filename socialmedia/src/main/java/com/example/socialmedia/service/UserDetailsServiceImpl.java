package com.example.socialmedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.socialmedia.entity.AppUsers;
import com.example.socialmedia.repository.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUsers appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                    .username(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles("USER")
                    .build();
    }

    public Long getUserIdByUsername(String username) throws UsernameNotFoundException {
        AppUsers appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return appUser.getUserId();
    }
}
