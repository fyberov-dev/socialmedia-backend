package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.auth.RegisterDto;
import ee.taltech.iti0302project.dto.user.UserDto;
import ee.taltech.iti0302project.dto.user.UserProfileDto;
import ee.taltech.iti0302project.dto.user.UserUpdateDTO;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.user.UserNotFoundException;
import ee.taltech.iti0302project.mapper.UserMapper;
import ee.taltech.iti0302project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUserByUsernameUserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("unknownUser"));
    }

    @Test
    void testGetUserProfileDto() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        UserProfileDto userProfileDto = new UserProfileDto();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userMapper.toUserProfileDto(user)).thenReturn(userProfileDto);

        UserProfileDto result = userService.getUserProfileDto("testUser");

        assertNotNull(result);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(userMapper, times(1)).toUserProfileDto(user);
    }

    @Test
    void testUpdateUserProfile() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");

        UserEntity updatedUser = new UserEntity();
        updatedUser.setName("UpdatedName");
        updatedUser.setSurname("UpdatedSurname");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        userService.updateUserProfile("testUser", updatedUser);

        verify(userRepository, times(1)).save(user);
        assertEquals("UpdatedName", user.getName());
        assertEquals("UpdatedSurname", user.getSurname());
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("updatedUser", user.getUsername());
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testDeleteUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        userService.deleteUser("testUser");

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testGetUserUpdateDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        UserUpdateDTO inputDto = new UserUpdateDTO();
        inputDto.setPassword("newPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        UserUpdateDTO result = userService.getUserUpdateDTO("testUser", inputDto);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("newPassword", result.getPassword());
    }

    @Test
    void testGetUserDelete() {
        when(userRepository.existsByUsername("testUser")).thenReturn(false);

        boolean result = userService.getUserDelete("testUser");

        assertFalse(result);
        verify(userRepository, times(1)).existsByUsername("testUser");
    }

    @Test
    void testGetCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserEntity result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }
}
