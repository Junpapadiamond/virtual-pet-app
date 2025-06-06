package com.snowleopard.virtual_pet.entity;

import com.snowleopard.virtual_pet.enums.ActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activities")
public class Activity {
    
    @Id
    private String id;
    
    @NotBlank(message = "Pet ID is required")
    @Indexed
    private String petId;
    
    @NotBlank(message = "User ID is required")
    @Indexed
    private String userId;
    
    @NotNull(message = "Activity type is required")
    private ActivityType type;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    // Activity results
    private Map<String, Integer> statChanges = new HashMap<>();
    private Integer experienceGained = 0;
    private Double coinsEarned = 0.0;
    
    // Activity context
    private Map<String, Object> activityData = new HashMap<>();
    private Integer duration = 0; // in minutes
    private Boolean wasSuccessful = true;
    
    @CreatedDate
    @Indexed
    private LocalDateTime timestamp;
    
    // Utility methods
    public void addStatChange(String stat, Integer change) {
        statChanges.put(stat, change);
    }
    
    public Integer getStatChange(String stat) {
        return statChanges.getOrDefault(stat, 0);
    }
}
