package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.auth.LoginDto;
import ee.taltech.iti0302project.dto.auth.RegisterDto;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.InvalidUserException;
import ee.taltech.iti0302project.mapper.UserMapper;
import ee.taltech.iti0302project.repository.UserRepository;
import ee.taltech.iti0302project.security.JwtGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtGenerator jwtGenerator;

    private RegisterDto registerDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setUsername("nikita");
        registerDto.setPassword("Qwerty123456");
        registerDto.setEmail("nikita@gmail.com");
        registerDto.setName("Nikita");
        registerDto.setSurname("Nikita");

        loginDto = new LoginDto();
        loginDto.setUsername("nikita");
        loginDto.setPassword("Qwerty123456");
    }

    @Test
    void testRegisterUserSuccess() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("nikita");
        userEntity.setPassword("Qwerty123456");

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userMapper.fromRegisterDto(registerDto)).thenReturn(userEntity);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("Qwerty123456");

        authService.registerUser(registerDto);

        verify(userRepository).save(userEntity);
    }

    @Test
    void testRegisterUserInvalidPasswordThrowsException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testUser");
        registerDto.setPassword("short");
        registerDto.setEmail("test@example.com");
        registerDto.setName("Test");
        registerDto.setSurname("User");

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> authService.registerUser(registerDto));
        assertEquals("Password must be at least 8 characters long.", exception.getMessage());
    }

    @Test
    void testLoginUserSuccess() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("TestPass123");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("jwtToken");

        String token = authService.loginUser(loginDto);

        assertEquals("jwtToken", token);
    }

    @Test
    void testLoginUserBadCredentialsThrowsException() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> authService.loginUser(loginDto));
    }

    @Test
    void testValidateUserRegistrationUsernameExistsThrowsException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("existingUser");
        registerDto.setPassword("TestPass123");
        registerDto.setEmail("test@example.com");
        registerDto.setName("Test");
        registerDto.setSurname("User");

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> authService.validateUserRegistration(registerDto));
        assertEquals("Username already exists.", exception.getMessage());
    }

    @Test
    void testValidateUserRegistrationEmailExistsThrowsException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testUser");
        registerDto.setPassword("TestPass123");
        registerDto.setEmail("existing@example.com");
        registerDto.setName("Test");
        registerDto.setSurname("User");

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> authService.validateUserRegistration(registerDto));
        assertEquals("Email already exists.", exception.getMessage());
    }

    @Test
    void testValidateUserRegistrationPasswordContainsNameThrowsException() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testUser");
        registerDto.setPassword("TestUser123");
        registerDto.setEmail("test@example.com");
        registerDto.setName("Test");
        registerDto.setSurname("User");

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> authService.validateUserRegistration(registerDto));
        assertEquals("Password must not contain username or surname.", exception.getMessage());
    }

    @Test
    void testRegisterUserPasswordDoesNotMeetComplexityThrowsException() {
        registerDto.setPassword("qwerty123");
        InvalidUserException exception1 = assertThrows(InvalidUserException.class, () -> authService.registerUser(registerDto));
        assertEquals("Password must contain at least one uppercase letter, one lowercase letter, and one digit.", exception1.getMessage());
    }
}
