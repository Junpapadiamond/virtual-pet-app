package com.snowleopard.virtual_pet.dto.request;

import com.snowleopard.virtual_pet.enums.PetType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetRequest {
    
    @NotBlank(message = "Pet name is required")
    @Size(min = 1, max = 20, message = "Pet name must be between 1 and 20 characters")
    private String name;
    
    @NotNull(message = "Pet type is required")
    private PetType type;
    
    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;
}
