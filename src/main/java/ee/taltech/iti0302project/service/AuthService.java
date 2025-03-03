package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.auth.LoginDto;
import ee.taltech.iti0302project.dto.auth.RegisterDto;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.InvalidUserException;
import ee.taltech.iti0302project.mapper.UserMapper;
import ee.taltech.iti0302project.repository.UserRepository;
import ee.taltech.iti0302project.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    public void registerUser(RegisterDto registerDto) {
        log.info("Registering user with username: {}", registerDto.getUsername());
        try {
            validateUserRegistration(registerDto);
            UserEntity userEntity = userMapper.fromRegisterDto(registerDto);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);
            log.info("User registered successfully: {}", registerDto.getUsername());
        } catch (InvalidUserException e) {
            log.error("User registration failed: {}", e.getMessage());
            throw e;
        }
    }

    public String loginUser(LoginDto loginDto) throws BadCredentialsException {
        log.info("Attempting to log in user: {}", loginDto.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            log.info("User logged in successfully: {}", loginDto.getUsername());
            return token;
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user: {}", loginDto.getUsername());
            throw new BadCredentialsException("Bad credentials");
        }
    }

    public void validateUserRegistration(RegisterDto registerDto) throws InvalidUserException {
        log.info("Validating registration for user: {}", registerDto.getUsername());

        if (registerDto.getPassword().length() < 8) {
            log.warn("Validation failed: Password too short for user: {}", registerDto.getUsername());
            throw new InvalidUserException("Password must be at least 8 characters long.");
        }

        if (!Pattern.compile("[A-Z]").matcher(registerDto.getPassword()).find() ||
                !Pattern.compile("[a-z]").matcher(registerDto.getPassword()).find() ||
                !Pattern.compile("\\d").matcher(registerDto.getPassword()).find()) {
            log.warn("Validation failed: Password does not meet complexity requirements for user: {}", registerDto.getUsername());
            throw new InvalidUserException("Password must contain at least one uppercase letter, one lowercase letter, and one digit.");
        }

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            log.warn("Validation failed: Username already exists for user: {}", registerDto.getUsername());
            throw new InvalidUserException("Username already exists.");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            log.warn("Validation failed: Email already exists for user: {}", registerDto.getEmail());
            throw new InvalidUserException("Email already exists.");
        }

        if (registerDto.getPassword().toLowerCase().contains(registerDto.getName().toLowerCase()) ||
                registerDto.getPassword().toLowerCase().contains(registerDto.getSurname().toLowerCase())) {
            log.warn("Validation failed: Password contains user's name or surname for user: {}", registerDto.getUsername());
            throw new InvalidUserException("Password must not contain username or surname.");
        }

        log.info("Validation passed for user: {}", registerDto.getUsername());
    }
}
