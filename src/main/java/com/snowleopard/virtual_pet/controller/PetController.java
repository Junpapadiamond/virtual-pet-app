package com.snowleopard.virtual_pet.controller;

import com.snowleopard.virtual_pet.dto.request.CreatePetRequest;
import com.snowleopard.virtual_pet.dto.response.ApiResponse;
import com.snowleopard.virtual_pet.dto.response.PetResponse;
import com.snowleopard.virtual_pet.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse<PetResponse>> getPet(@PathVariable String petId) {
        log.info("Getting pet with id: {}", petId);
        PetResponse pet = petService.getPetById(petId);
        return ResponseEntity.ok(ApiResponse.success("Pet retrieved successfully", pet));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<PetResponse>>> getPetsByOwner(@PathVariable String ownerId) {
        log.info("Getting pets for owner: {}", ownerId);
        List<PetResponse> pets = petService.getPetsByOwnerId(ownerId);
        return ResponseEntity.ok(ApiResponse.success("Pets retrieved successfully", pets));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PetResponse>> createPet(
            @Valid @RequestBody CreatePetRequest request,
            @RequestHeader("User-Id") String userId) {
        log.info("Creating new pet for user: {}", userId);
        PetResponse pet = petService.createPet(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pet created successfully", pet));
    }

    @PostMapping("/{petId}/feed")
    public ResponseEntity<ApiResponse<PetResponse>> feedPet(
            @PathVariable String petId,
            @RequestParam(defaultValue = "regular food") String foodType,
            @RequestHeader("User-Id") String userId) {
        log.info("Feeding pet {} with {}", petId, foodType);
        PetResponse pet = petService.feedPet(petId, foodType, userId);
        return ResponseEntity.ok(ApiResponse.success("Pet fed successfully", pet));
    }

    @PostMapping("/{petId}/play")
    public ResponseEntity<ApiResponse<PetResponse>> playWithPet(
            @PathVariable String petId,
            @RequestHeader("User-Id") String userId) {
        log.info("Playing with pet {}", petId);
        PetResponse pet = petService.playWithPet(petId, userId);
        return ResponseEntity.ok(ApiResponse.success("Played with pet successfully", pet));
    }

    @GetMapping("/owner/{ownerId}/attention")
    public ResponseEntity<ApiResponse<List<PetResponse>>> getPetsNeedingAttention(@PathVariable String ownerId) {
        log.info("Getting pets needing attention for owner: {}", ownerId);
        List<PetResponse> pets = petService.getPetsNeedingAttention(ownerId);
        return ResponseEntity.ok(ApiResponse.success("Pets needing attention retrieved", pets));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Pet service is healthy"));
    }
}
