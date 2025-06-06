package com.snowleopard.virtual_pet.controller;

import com.snowleopard.virtual_pet.dto.response.ApiResponse;
import com.snowleopard.virtual_pet.dto.response.PetResponse;
import com.snowleopard.virtual_pet.dto.response.UserResponse;
import com.snowleopard.virtual_pet.enums.PetType;
import com.snowleopard.virtual_pet.enums.ActivityType;
import com.snowleopard.virtual_pet.service.PetService;
import com.snowleopard.virtual_pet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final PetService petService;
    private final UserService userService;

    @GetMapping("/pet-types")
    public ResponseEntity<ApiResponse<List<PetType>>> getPetTypes() {
        List<PetType> petTypes = Arrays.asList(PetType.values());
        return ResponseEntity.ok(ApiResponse.success("Pet types retrieved successfully", petTypes));
    }

    @GetMapping("/activity-types")
    public ResponseEntity<ApiResponse<List<ActivityType>>> getActivityTypes() {
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values());
        return ResponseEntity.ok(ApiResponse.success("Activity types retrieved successfully", activityTypes));
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard(@PathVariable String userId) {
        log.info("Getting dashboard for user: {}", userId);
        
        Map<String, Object> dashboard = new HashMap<>();
        
        // Get user info
        UserResponse user = userService.getUserById(userId);
        dashboard.put("user", user);
        
        // Get user's pets
        List<PetResponse> pets = petService.getPetsByOwnerId(userId);
        dashboard.put("pets", pets);
        
        // Get pets needing attention
        List<PetResponse> petsNeedingAttention = petService.getPetsNeedingAttention(userId);
        dashboard.put("petsNeedingAttention", petsNeedingAttention);
        
        // Game statistics
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPets", pets.size());
        stats.put("petsNeedingAttention", petsNeedingAttention.size());
        stats.put("totalLevel", pets.stream().mapToInt(PetResponse::getLevel).sum());
        stats.put("averageWellbeing", pets.stream().mapToInt(PetResponse::getOverallWellbeing).average().orElse(0));
        dashboard.put("statistics", stats);
        
        return ResponseEntity.ok(ApiResponse.success("Dashboard retrieved successfully", dashboard));
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<ApiResponse<List<String>>> getRecommendations(@PathVariable String userId) {
        log.info("Getting recommendations for user: {}", userId);
        
        List<PetResponse> pets = petService.getPetsByOwnerId(userId);
        List<String> recommendations = pets.stream()
                .flatMap(pet -> pet.getRecommendations().stream())
                .distinct()
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("Recommendations retrieved successfully", recommendations));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Game service is healthy"));
    }
}
