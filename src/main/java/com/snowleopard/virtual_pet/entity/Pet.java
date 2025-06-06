package com.snowleopard.virtual_pet.entity;

import com.snowleopard.virtual_pet.enums.PetType;
import com.snowleopard.virtual_pet.enums.PetMood;
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
import java.util.HashMap;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pets")
public class Pet {
    
    @Id
    private String id;
    
    @NotBlank(message = "Pet name is required")
    @Size(min = 1, max = 20, message = "Pet name must be between 1 and 20 characters")
    private String name;
    
    @NotBlank(message = "Owner ID is required")
    @Indexed
    private String ownerId;
    
    @NotNull(message = "Pet type is required")
    private PetType type;
    
    // Basic stats (0-100)
    @Min(value = 0, message = "Hunger must be at least 0")
    @Max(value = 100, message = "Hunger cannot exceed 100")
    private Integer hunger = 100;
    
    @Min(value = 0, message = "Happiness must be at least 0")
    @Max(value = 100, message = "Happiness cannot exceed 100")
    private Integer happiness = 100;
    
    @Min(value = 0, message = "Health must be at least 0")
    @Max(value = 100, message = "Health cannot exceed 100")
    private Integer health = 100;
    
    @Min(value = 0, message = "Energy must be at least 0")
    @Max(value = 100, message = "Energy cannot exceed 100")
    private Integer energy = 100;
    
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience = 0;
    
    @Min(value = 1, message = "Level must be at least 1")
    private Integer level = 1;
    
    // Personality traits (0.0 to 1.0)
    private Map<String, Double> personality = new HashMap<>();
    
    private PetMood currentMood = PetMood.CONTENT;
    
    private List<String> preferences = new ArrayList<>();
    
    // Game mechanics
    private List<String> unlockedAchievements = new ArrayList<>();
    private Map<String, Object> inventory = new HashMap<>();
    
    @Min(value = 0, message = "Coins cannot be negative")
    private Double coins = 100.0;
    
    // Activity tracking
    private Integer totalActivities = 0;
    private Integer totalFeedings = 0;
    private Integer totalPlaySessions = 0;
    private Integer totalTrainingSessions = 0;
    
    // Timestamps
    private LocalDateTime birthDate;
    private LocalDateTime lastFed;
    private LocalDateTime lastPlayed;
    private LocalDateTime lastTrained;
    private LocalDateTime lastHealthCheck;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Utility methods
    public boolean isHungry() {
        return hunger < 30;
    }
    
    public boolean isUnhappy() {
        return happiness < 30;
    }
    
    public boolean isTired() {
        return energy < 20;
    }
    
    public boolean isSick() {
        return health < 50;
    }
    
    public boolean needsAttention() {
        return isHungry() || isUnhappy() || isSick();
    }
    
    public int getOverallWellbeing() {
        return (hunger + happiness + health + energy) / 4;
    }
    
    public PetMood calculateMood() {
        int moodScore = getOverallWellbeing();
        return PetMood.fromScore(moodScore);
    }
    
    public boolean canLevelUp() {
        return experience >= (level * 100);
    }
    
    public void levelUp() {
        if (canLevelUp()) {
            level++;
            experience -= ((level - 1) * 100);
            // Bonus stats on level up
            health = Math.min(100, health + 10);
            happiness = Math.min(100, happiness + 5);
        }
    }
}
