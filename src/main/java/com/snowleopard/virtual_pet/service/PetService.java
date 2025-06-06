package com.snowleopard.virtual_pet.service;

import com.snowleopard.virtual_pet.dto.request.CreatePetRequest;
import com.snowleopard.virtual_pet.dto.request.ActivityRequest;
import com.snowleopard.virtual_pet.dto.response.PetResponse;
import com.snowleopard.virtual_pet.entity.Pet;
import com.snowleopard.virtual_pet.enums.PetType;
import com.snowleopard.virtual_pet.enums.ActivityType;
import com.snowleopard.virtual_pet.exception.PetNotFoundException;
import com.snowleopard.virtual_pet.exception.InsufficientResourcesException;
import com.snowleopard.virtual_pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Cacheable(value = "pets", key = "#petId")
    public PetResponse getPetById(String petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + petId));
        return convertToPetResponse(pet);
    }

    public List<PetResponse> getPetsByOwnerId(String ownerId) {
        List<Pet> pets = petRepository.findByOwnerIdOrderByLevelDesc(ownerId);
        return pets.stream()
                .map(this::convertToPetResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PetResponse createPet(CreatePetRequest request, String ownerId) {
        log.info("Creating new pet for user: {}", ownerId);
        
        // Check if pet name already exists for this owner
        if (petRepository.existsByOwnerIdAndName(ownerId, request.getName())) {
            throw new IllegalArgumentException("Pet with this name already exists");
        }

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setType(request.getType());
        pet.setOwnerId(ownerId);
        pet.setBirthDate(LocalDateTime.now());
        
        // Initialize personality based on pet type
        pet.setPersonality(generatePersonality(request.getType()));
        pet.setPreferences(generatePreferences(request.getType()));
        
        Pet savedPet = petRepository.save(pet);
        log.info("Pet created successfully: {}", savedPet.getId());
        
        return convertToPetResponse(savedPet);
    }

    @Transactional
    @CacheEvict(value = "pets", key = "#petId")
    public PetResponse feedPet(String petId, String foodType, String userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + petId));

        if (!pet.getOwnerId().equals(userId)) {
            throw new IllegalArgumentException("You don't own this pet");
        }

        // Calculate food effects based on pet type and food type
        int hungerIncrease = calculateFoodEffect(pet.getType(), foodType);
        
        pet.setHunger(Math.min(100, pet.getHunger() + hungerIncrease));
        pet.setHappiness(Math.min(100, pet.getHappiness() + 5));
        pet.setLastFed(LocalDateTime.now());
        pet.setTotalFeedings(pet.getTotalFeedings() + 1);
        
        // Update mood
        pet.setCurrentMood(pet.calculateMood());
        
        Pet updatedPet = petRepository.save(pet);
        log.info("Pet {} fed with {}", petId, foodType);
        
        return convertToPetResponse(updatedPet);
    }

    @Transactional
    @CacheEvict(value = "pets", key = "#petId")
    public PetResponse playWithPet(String petId, String userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + petId));

        if (!pet.getOwnerId().equals(userId)) {
            throw new IllegalArgumentException("You don't own this pet");
        }

        if (pet.getEnergy() < 15) {
            throw new InsufficientResourcesException("Pet is too tired to play");
        }

        // Apply play effects
        pet.setEnergy(Math.max(0, pet.getEnergy() - 15));
        pet.setHappiness(Math.min(100, pet.getHappiness() + 10));
        pet.setExperience(pet.getExperience() + 5);
        pet.setLastPlayed(LocalDateTime.now());
        pet.setTotalPlaySessions(pet.getTotalPlaySessions() + 1);
        
        // Check for level up
        if (pet.canLevelUp()) {
            pet.levelUp();
        }
        
        // Update mood
        pet.setCurrentMood(pet.calculateMood());
        
        Pet updatedPet = petRepository.save(pet);
        log.info("Played with pet {}", petId);
        
        return convertToPetResponse(updatedPet);
    }

    public List<PetResponse> getPetsNeedingAttention(String ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        return pets.stream()
                .filter(Pet::needsAttention)
                .map(this::convertToPetResponse)
                .collect(Collectors.toList());
    }

    // Private helper methods
    private Map<String, Double> generatePersonality(PetType type) {
        Map<String, Double> personality = new HashMap<>();
        Random random = new Random();
        
        switch (type) {
            case DOG -> {
                personality.put("playful", 0.7 + random.nextDouble() * 0.3);
                personality.put("loyal", 0.8 + random.nextDouble() * 0.2);
                personality.put("social", 0.6 + random.nextDouble() * 0.4);
                personality.put("energetic", 0.7 + random.nextDouble() * 0.3);
            }
            case CAT -> {
                personality.put("independent", 0.7 + random.nextDouble() * 0.3);
                personality.put("curious", 0.6 + random.nextDouble() * 0.4);
                personality.put("lazy", 0.4 + random.nextDouble() * 0.4);
                personality.put("mysterious", 0.8 + random.nextDouble() * 0.2);
            }
            case DRAGON -> {
                personality.put("powerful", 0.9 + random.nextDouble() * 0.1);
                personality.put("wise", 0.8 + random.nextDouble() * 0.2);
                personality.put("protective", 0.8 + random.nextDouble() * 0.2);
                personality.put("magical", 0.9 + random.nextDouble() * 0.1);
            }
            case ROBOT -> {
                personality.put("logical", 0.9 + random.nextDouble() * 0.1);
                personality.put("efficient", 0.8 + random.nextDouble() * 0.2);
                personality.put("helpful", 0.7 + random.nextDouble() * 0.3);
                personality.put("analytical", 0.8 + random.nextDouble() * 0.2);
            }
            case FAIRY -> {
                personality.put("magical", 0.9 + random.nextDouble() * 0.1);
                personality.put("gentle", 0.8 + random.nextDouble() * 0.2);
                personality.put("mischievous", 0.5 + random.nextDouble() * 0.5);
                personality.put("ethereal", 0.9 + random.nextDouble() * 0.1);
            }
        }
        
        return personality;
    }

    private List<String> generatePreferences(PetType type) {
        return switch (type) {
            case DOG -> Arrays.asList("playing fetch", "belly rubs", "treats", "walks");
            case CAT -> Arrays.asList("napping", "climbing", "fish treats", "laser pointers");
            case DRAGON -> Arrays.asList("treasure hunting", "flying", "magical items", "riddles");
            case ROBOT -> Arrays.asList("upgrades", "maintenance", "data processing", "efficiency");
            case FAIRY -> Arrays.asList("flowers", "music", "dancing", "nature magic");
        };
    }

    private int calculateFoodEffect(PetType petType, String foodType) {
        // Base food effect
        int baseEffect = 20;
        
        // Type-specific bonuses
        return switch (petType) {
            case DOG -> foodType.contains("meat") ? baseEffect + 10 : baseEffect;
            case CAT -> foodType.contains("fish") ? baseEffect + 10 : baseEffect;
            case DRAGON -> foodType.contains("magical") ? baseEffect + 15 : baseEffect;
            case ROBOT -> foodType.contains("energy") ? baseEffect + 10 : baseEffect;
            case FAIRY -> foodType.contains("nectar") ? baseEffect + 10 : baseEffect;
        };
    }

    private PetResponse convertToPetResponse(Pet pet) {
        PetResponse response = new PetResponse();
        response.setId(pet.getId());
        response.setName(pet.getName());
        response.setOwnerId(pet.getOwnerId());
        response.setType(pet.getType());
        response.setHunger(pet.getHunger());
        response.setHappiness(pet.getHappiness());
        response.setHealth(pet.getHealth());
        response.setEnergy(pet.getEnergy());
        response.setExperience(pet.getExperience());
        response.setLevel(pet.getLevel());
        response.setPersonality(pet.getPersonality());
        response.setCurrentMood(pet.getCurrentMood());
        response.setPreferences(pet.getPreferences());
        response.setUnlockedAchievements(pet.getUnlockedAchievements());
        response.setCoins(pet.getCoins());
        response.setTotalActivities(pet.getTotalActivities());
        response.setNeedsAttention(pet.needsAttention());
        response.setIsHungry(pet.isHungry());
        response.setIsUnhappy(pet.isUnhappy());
        response.setIsTired(pet.isTired());
        response.setIsSick(pet.isSick());
        response.setOverallWellbeing(pet.getOverallWellbeing());
        response.setBirthDate(pet.getBirthDate());
        response.setLastFed(pet.getLastFed());
        response.setLastPlayed(pet.getLastPlayed());
        response.setCreatedAt(pet.getCreatedAt());
        response.setUpdatedAt(pet.getUpdatedAt());
        response.setRecommendations(generateRecommendations(pet));
        
        return response;
    }

    private List<String> generateRecommendations(Pet pet) {
        List<String> recommendations = new ArrayList<>();
        
        if (pet.getHunger() < 30) {
            recommendations.add("Your pet is hungry! Feed them some " + pet.getPreferences().get(0));
        }
        if (pet.getHappiness() < 50) {
            recommendations.add("Your pet seems bored. Try playing with them!");
        }
        if (pet.getEnergy() < 20) {
            recommendations.add("Your pet needs rest. Let them sleep for a while.");
        }
        if (pet.getHealth() < 60) {
            recommendations.add("Consider taking your pet for a health check.");
        }
        if (pet.canLevelUp()) {
            recommendations.add("Your pet is ready to level up! Gain more experience through activities.");
        }
        
        return recommendations;
    }
}
