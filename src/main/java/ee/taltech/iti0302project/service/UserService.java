package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.user.UserDto;
import ee.taltech.iti0302project.dto.user.UserProfileDto;
import ee.taltech.iti0302project.dto.user.UserUpdateDTO;
import ee.taltech.iti0302project.entity.UserEntity;
import ee.taltech.iti0302project.exceptions.user.UserNotFoundException;
import ee.taltech.iti0302project.mapper.UserMapper;
import ee.taltech.iti0302project.repository.UserRepository;
import ee.taltech.iti0302project.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserProfileDto getUserProfileDto(String username) {
        return userMapper.toUserProfileDto(getUserByUsername(username));
    }

    public UserUpdateDTO getUserUpdateDTO(String username, UserUpdateDTO user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        UserEntity existingUser = getUserByUsername(username);
        dto.setUsername(username);
        dto.setPassword(user.getPassword());
        return dto;
    }

    public boolean getUserDelete(String username) {
        deleteUser(username);
        return userRepository.existsByUsername(username);
    }

    public void updateUserProfile(String username, UserEntity updatedUser) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword()); // Assuming password should be updated
            userRepository.save(user);
        }
    }

    public void deleteUser(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(userRepository::delete);
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }
}
