package com.snowleopard.virtual_pet.service;

import com.snowleopard.virtual_pet.dto.request.CreateUserRequest;
import com.snowleopard.virtual_pet.dto.response.UserResponse;
import com.snowleopard.virtual_pet.entity.User;
import com.snowleopard.virtual_pet.exception.UserNotFoundException;
import com.snowleopard.virtual_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return convertToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return convertToUserResponse(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Add null checks
        if (request == null) {
            throw new IllegalArgumentException("CreateUserRequest cannot be null");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        log.info("Creating new user: {}", request.getUsername());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTimezone(request.getTimezone());
        user.setLastLoginDate(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getId());
        
        return convertToUserResponse(savedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse updateLastLogin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        user.setLastLoginDate(LocalDateTime.now());
        user.setTotalLoginDays(user.getTotalLoginDays() + 1);
        
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse addExperience(String userId, Integer experience) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        user.setTotalExperience(user.getTotalExperience() + experience);
        
        // Check for level up
        if (user.canLevelUp()) {
            user.levelUp();
            log.info("User {} leveled up to level {}", userId, user.getPlayerLevel());
        }
        
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse addPet(String userId, String petId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        if (!user.canAdoptMorePets()) {
            throw new IllegalArgumentException("Maximum pet limit reached");
        }
        
        user.getPetIds().add(petId);
        User updatedUser = userRepository.save(user);
        
        return convertToUserResponse(updatedUser);
    }

    public boolean validateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return passwordEncoder.matches(password, userOpt.get().getPassword());
        }
        return false;
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setTotalCoins(user.getTotalCoins());
        response.setTotalExperience(user.getTotalExperience());
        response.setPlayerLevel(user.getPlayerLevel());
        response.setPetIds(user.getPetIds());
        response.setMaxPets(user.getMaxPets());
        response.setCurrentPetCount(user.getPetIds().size());
        response.setUnlockedAchievements(user.getUnlockedAchievements());
        response.setActivityCounts(user.getActivityCounts());
        response.setTimezone(user.getTimezone());
        response.setNotificationsEnabled(user.getNotificationsEnabled());
        response.setTotalLoginDays(user.getTotalLoginDays());
        response.setLastLoginDate(user.getLastLoginDate());
        response.setTotalTimeSpent(user.getTotalTimeSpent());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        
        return response;
    }
}
