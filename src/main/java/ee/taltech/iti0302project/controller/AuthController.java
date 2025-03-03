package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.auth.AuthResponseDto;
import ee.taltech.iti0302project.dto.auth.LoginDto;
import ee.taltech.iti0302project.dto.auth.RegisterDto;
import ee.taltech.iti0302project.exceptions.InvalidUserException;
import ee.taltech.iti0302project.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Registers a new user with the provided registration details.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        try {
            authService.registerUser(registerDto);
            return ResponseEntity.ok("User registered successfully!");
        } catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Login a user", description = "Logs in a user with the provided credentials.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(new AuthResponseDto(authService.loginUser(loginDto)));
        } catch (BadCredentialsException ignored) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
