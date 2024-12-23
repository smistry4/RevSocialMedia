package com.example.socialmedia;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.socialmedia.entity.AppUsers;
import com.example.socialmedia.exception.ClientErrorException;
import com.example.socialmedia.exception.ResourceAlreadyExistsException;
import com.example.socialmedia.repository.AppUserRepository;
import com.example.socialmedia.repository.RelationshipRepository;
import com.example.socialmedia.security.JwtUtil;
import com.example.socialmedia.service.AppUserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private RelationshipRepository relationshipRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AppUserService appUserService;

    private AppUsers appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUsers();
        appUser.setUserId(1L);
        appUser.setUsername("testuser");
        appUser.setEmail("testuser@example.com");
        appUser.setPassword("password123");
        appUser.setFirstName("test");
        appUser.setLastName("user");
    }

    @Test
    public void testRegisterUser_ShouldReturnToken_WhenValid() throws ResourceAlreadyExistsException, ClientErrorException {
        // Arrange
        when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(appUser.getUsername())).thenReturn("dummy-jwt-token");

        // Act
        String token = appUserService.registerUser(appUser);

        // Assert
        assertEquals("dummy-jwt-token",token);
        verify(appUserRepository, times(1)).save(appUser);
    }

    @Test
    public void testRegisterUser_ShouldThrowException_UsernameAlreadyExist() throws ResourceAlreadyExistsException, ClientErrorException {
        // Arrange
        AppUsers user = new AppUsers();
        user.setUserId(17L);
        user.setUsername("testuser");
        user.setEmail("othertest@test.com");
        user.setPassword("thisisexception");

        when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> appUserService.registerUser(appUser));
        verifyNoInteractions(jwtUtil);
        verify(appUserRepository, never()).save(any(AppUsers.class));
    }

    @Test
    public void testRegisterUser_ShouldThrowException_EmailAlreadyExist() throws ResourceAlreadyExistsException, ClientErrorException {
        // Arrange
        AppUsers user = new AppUsers();
        user.setUserId(17L);
        user.setUsername("otherTest");
        user.setEmail("testuser@example.com");
        user.setPassword("thisisexception");

        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> appUserService.registerUser(appUser));
        verifyNoInteractions(jwtUtil);
        verify(appUserRepository, never()).save(any(AppUsers.class));
    }

    @Test
    public void testLoginUser_ShouldReturnToken_WhenValid() throws AuthenticationException {
        // Arrange
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateToken(appUser.getUsername())).thenReturn("dummy-jwt-token");

        // Act
        String token = appUserService.loginUser(appUser);

        // Assert
        assertEquals("dummy-jwt-token", token);
        verify(authenticationManager).authenticate(any());
    }

    @Test
    public void testLoginUser_ShouldThrowException_WhenAuthenticationFails() throws AuthenticationException {
        // Arrange
        AppUsers user = new AppUsers();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword());
        when(authenticationManager.authenticate(authToken)).thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> appUserService.loginUser(user));
    }


    @Test
    public void testUpdateUser_ShouldReturnToken_WhenValid() throws ResourceAlreadyExistsException, ClientErrorException {
        // Arrange
        AppUsers updatedUser = new AppUsers();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updateduser@example.com");
        updatedUser.setPassword("newpassword");

        when(appUserRepository.findById(updatedUser.getUserId())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(updatedUser.getUsername())).thenReturn("updated-jwt-token");

        // Act
        String token = appUserService.updateUser(updatedUser);

        // Assert
        assertEquals("updated-jwt-token", token);
        verify(appUserRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testGetUserById_ShouldReturnUser_WhenExists() {
        // Arrange
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(appUser));

        // Act
        AppUsers user = appUserService.getUserById(1L);

        // Assert
        assertEquals(appUser, user);
    }

    @Test
    public void testGetUserByUsername_ShouldReturnUser_WhenExists() {
        // Arrange
        when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.of(appUser));

        // Act
        AppUsers user = appUserService.getUserByUsername(appUser.getUsername());

        // Assert
        assertEquals(appUser, user);
    }

    @Test
    public void testGetUserById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(appUserRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientErrorException.class, () -> appUserService.getUserById(1L));
    }

    @Test
    public void testGetUserByUsername_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientErrorException.class, () -> appUserService.getUserByUsername(appUser.getUsername()));
    }

    @Test
    public void testSearchUser_ShouldReturnList_WhenQueryMatches() {
        // Arrange
        String query = "test";
        AppUsers appUser1 = new AppUsers();
        appUser1.setUsername("testuser1");
        appUser1.setEmail("testuser1@example.com");

        List<AppUsers> expectedUsers = Arrays.asList(appUser, appUser1);

        when(appUserRepository.searchByNameOrUsername(query)).thenReturn(expectedUsers);

        // Act
        List<AppUsers> actualUsers = appUserService.searchUser(query);

        // Assert
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testSearchUser_ShouldThrowException_WhenQueryEmpty() {
        // Arrange, Act & Assert
        assertThrows(ClientErrorException.class, () -> appUserService.searchUser(""));
    }

    @Test
    public void testFollowUser_ShouldThrowException_WhenSameIds() {
        // Arrange
        Long userId = 1L;
        Long followedId = 1L;

        // Act and Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> appUserService.followUser(userId, followedId));
        assertEquals("Cannot have same ids", ex.getMessage());
        verifyNoInteractions(relationshipRepository);
    }

    @Test
    public void testUnFollowUser_ShouldThrowException_WhenSameIds() {
        // Arrange
        Long userId = 1L;
        Long followedId = 1L;

        // Act and Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> appUserService.unfollowUser(userId, followedId));
        assertEquals("Cannot have same ids", ex.getMessage());
        verifyNoInteractions(relationshipRepository);
    }
}
