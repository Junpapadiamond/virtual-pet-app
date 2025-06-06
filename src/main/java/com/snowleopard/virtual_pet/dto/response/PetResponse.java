package com.snowleopard.virtual_pet.dto.response;

import com.snowleopard.virtual_pet.enums.PetType;
import com.snowleopard.virtual_pet.enums.PetMood;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    
    private String id;
    private String name;
    private String ownerId;
    private PetType type;
    
    // Stats
    private Integer hunger;
    private Integer happiness;
    private Integer health;
    private Integer energy;
    private Integer experience;
    private Integer level;
    
    // Personality and mood
    private Map<String, Double> personality;
    private PetMood currentMood;
    private List<String> preferences;
    
    // Game data
    private List<String> unlockedAchievements;
    private Double coins;
    private Integer totalActivities;
    
    // Status indicators
    private Boolean needsAttention;
    private Boolean isHungry;
    private Boolean isUnhappy;
    private Boolean isTired;
    private Boolean isSick;
    private Integer overallWellbeing;
    
    // Timestamps
    private LocalDateTime birthDate;
    private LocalDateTime lastFed;
    private LocalDateTime lastPlayed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Recommendations
    private List<String> recommendations;
}
