package com.snowleopard.virtual_pet.dto.request;

import com.snowleopard.virtual_pet.enums.ActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.*;
import java.util.Map;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {
    
    @NotBlank(message = "Pet ID is required")
    private String petId;
    
    @NotNull(message = "Activity type is required")
    private ActivityType activityType;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private Map<String, Object> activityData = new HashMap<>();
    
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 180, message = "Duration cannot exceed 180 minutes")
    private Integer duration = 5; // default 5 minutes
}
