package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.user.UserProfileDto;
import ee.taltech.iti0302project.dto.user.UserUpdateDTO;
import ee.taltech.iti0302project.response.UserUpdateResponse;
import ee.taltech.iti0302project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user profile", description = "Fetches the profile of a user by their username.")
    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfileDto(username));
    }

    @Operation(summary = "Update user profile", description = "Updates the profile details of a user.")
    @PutMapping("/{username}/profile")
    public ResponseEntity<UserUpdateResponse> updateUserProfile(@PathVariable String username, @RequestBody UserUpdateDTO user) {
        UserUpdateDTO dto = userService.getUserUpdateDTO(username, user);
        return ResponseEntity.ok(new UserUpdateResponse(dto, "Profile updated successfully!"));
    }

    @Operation(summary = "Delete user", description = "Deletes a user from the system by their username.")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        String answer = "User is not deleted!";
        boolean deleted = userService.getUserDelete(username);
        if (deleted) {
            answer = "User deleted successfully!";
        }
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(Authentication authentication) {
        String username = authentication.getName();
        UserProfileDto dto = userService.getUserProfileDto(username);
        return ResponseEntity.ok(dto);
    }

}
