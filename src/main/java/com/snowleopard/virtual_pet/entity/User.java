package com.snowleopard.virtual_pet.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Indexed(unique = true)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Indexed(unique = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    // Game stats
    @Min(value = 0, message = "Total coins cannot be negative")
    private Double totalCoins = 500.0;
    
    @Min(value = 0, message = "Total experience cannot be negative")
    private Integer totalExperience = 0;
    
    @Min(value = 1, message = "Player level must be at least 1")
    private Integer playerLevel = 1;
    
    // Pet management
    private List<String> petIds = new ArrayList<>();
    
    @Max(value = 10, message = "Cannot have more than 10 pets")
    private Integer maxPets = 5;
    
    // Achievements and progress
    private List<String> unlockedAchievements = new ArrayList<>();
    private Map<String, Object> gameProgress = new HashMap<>();
    private Map<String, Integer> activityCounts = new HashMap<>();
    
    // User preferences
    private Map<String, Object> preferences = new HashMap<>();
    private String timezone = "UTC";
    private Boolean notificationsEnabled = true;
    
    // Statistics
    private Integer totalLoginDays = 0;
    private LocalDateTime lastLoginDate;
    private Integer totalTimeSpent = 0; // in minutes
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Utility methods
    public boolean canAdoptMorePets() {
        return petIds.size() < maxPets;
    }
    
    public boolean hasEnoughCoins(double amount) {
        return totalCoins >= amount;
    }
    
    public void spendCoins(double amount) {
        if (hasEnoughCoins(amount)) {
            totalCoins -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient coins");
        }
    }
    
    public void earnCoins(double amount) {
        totalCoins += amount;
    }
    
    public boolean canLevelUp() {
        return totalExperience >= (playerLevel * 1000);
    }
    
    public void levelUp() {
        if (canLevelUp()) {
            playerLevel++;
            totalExperience -= ((playerLevel - 1) * 1000);
            maxPets = Math.min(10, maxPets + 1);
            earnCoins(100 * playerLevel);
        }
    }
}
