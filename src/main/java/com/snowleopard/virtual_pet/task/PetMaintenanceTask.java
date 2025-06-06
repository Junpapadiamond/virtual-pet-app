package com.snowleopard.virtual_pet.task;

import com.snowleopard.virtual_pet.entity.Pet;
import com.snowleopard.virtual_pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PetMaintenanceTask {

    private final PetRepository petRepository;

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void degradePetStats() {
        log.info("Running pet maintenance task...");
        
        List<Pet> allPets = petRepository.findAll();
        int updatedPets = 0;
        
        for (Pet pet : allPets) {
            boolean updated = false;
            
            // Natural stat degradation over time
            if (pet.getHunger() > 0) {
                pet.setHunger(Math.max(0, pet.getHunger() - 2));
                updated = true;
            }
            
            if (pet.getHappiness() > 0) {
                pet.setHappiness(Math.max(0, pet.getHappiness() - 1));
                updated = true;
            }
            
            // Energy regeneration during rest
            if (isResting(pet) && pet.getEnergy() < 100) {
                pet.setEnergy(Math.min(100, pet.getEnergy() + 5));
                updated = true;
            }
            
            // Health deterioration if basic needs aren't met
            if (pet.getHunger() < 20 || pet.getHappiness() < 20) {
                pet.setHealth(Math.max(0, pet.getHealth() - 1));
                updated = true;
            }
            
            // Update mood based on current stats
            pet.setCurrentMood(pet.calculateMood());
            
            if (updated) {
                petRepository.save(pet);
                updatedPets++;
            }
        }
        
        if (updatedPets > 0) {
            log.info("Updated stats for {} pets", updatedPets);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
    public void dailyMaintenance() {
        log.info("Running daily pet maintenance...");
        
        List<Pet> allPets = petRepository.findAll();
        
        for (Pet pet : allPets) {
            // Small daily health bonus for well-cared pets
            if (pet.getOverallWellbeing() > 80) {
                pet.setHealth(Math.min(100, pet.getHealth() + 1));
                petRepository.save(pet);
            }
        }
        
        log.info("Daily maintenance completed for {} pets", allPets.size());
    }

    private boolean isResting(Pet pet) {
        return pet.getLastPlayed() == null || 
               pet.getLastPlayed().isBefore(LocalDateTime.now().minusHours(1));
    }
}
