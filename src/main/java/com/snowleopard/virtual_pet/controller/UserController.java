package com.snowleopard.virtual_pet.controller;

import com.snowleopard.virtual_pet.dto.request.CreateUserRequest;
import com.snowleopard.virtual_pet.dto.response.ApiResponse;
import com.snowleopard.virtual_pet.dto.response.UserResponse;
import com.snowleopard.virtual_pet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {
        log.info("Getting user with id: {}", userId);
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        log.info("Getting user with username: {}", username);
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Registering new user: {}", request.getUsername());
        UserResponse user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", user));
    }

    @PostMapping("/{userId}/login")
    public ResponseEntity<ApiResponse<UserResponse>> updateLastLogin(@PathVariable String userId) {
        log.info("Updating last login for user: {}", userId);
        UserResponse user = userService.updateLastLogin(userId);
        return ResponseEntity.ok(ApiResponse.success("Login updated successfully", user));
    }

    @PostMapping("/{userId}/experience")
    public ResponseEntity<ApiResponse<UserResponse>> addExperience(
            @PathVariable String userId,
            @RequestParam Integer experience) {
        log.info("Adding {} experience to user: {}", experience, userId);
        UserResponse user = userService.addExperience(userId, experience);
        return ResponseEntity.ok(ApiResponse.success("Experience added successfully", user));
    }

    @PostMapping("/{userId}/pets/{petId}")
    public ResponseEntity<ApiResponse<UserResponse>> addPetToUser(
            @PathVariable String userId,
            @PathVariable String petId) {
        log.info("Adding pet {} to user: {}", petId, userId);
        UserResponse user = userService.addPet(userId, petId);
        return ResponseEntity.ok(ApiResponse.success("Pet added to user successfully", user));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("User service is healthy"));
    }
}
