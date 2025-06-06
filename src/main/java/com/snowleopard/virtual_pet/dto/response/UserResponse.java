package com.snowleopard.virtual_pet.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private String id;
    private String username;
    private String email;
    
    // Game stats
    private Double totalCoins;
    private Integer totalExperience;
    private Integer playerLevel;
    
    // Pet management
    private List<String> petIds;
    private Integer maxPets;
    private Integer currentPetCount;
    
    // Achievements
    private List<String> unlockedAchievements;
    private Map<String, Integer> activityCounts;
    
    // User preferences
    private String timezone;
    private Boolean notificationsEnabled;
    
    // Statistics
    private Integer totalLoginDays;
    private LocalDateTime lastLoginDate;
    private Integer totalTimeSpent;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
