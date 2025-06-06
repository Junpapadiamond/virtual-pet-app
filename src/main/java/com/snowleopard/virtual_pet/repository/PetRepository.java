package com.snowleopard.virtual_pet.repository;

import com.snowleopard.virtual_pet.entity.Pet;
import com.snowleopard.virtual_pet.enums.PetType;
import com.snowleopard.virtual_pet.enums.PetMood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends MongoRepository<Pet, String> {
    
    List<Pet> findByOwnerId(String ownerId);
    
    List<Pet> findByOwnerIdOrderByLevelDesc(String ownerId);
    
    List<Pet> findByType(PetType type);
    
    List<Pet> findByCurrentMood(PetMood mood);
    
    @Query("{'ownerId': ?0, 'level': {$gte: ?1}}")
    List<Pet> findByOwnerIdAndLevelGreaterThanEqual(String ownerId, Integer level);
    
    @Query("{'hunger': {$lt: ?0}}")
    List<Pet> findHungryPets(Integer hungerThreshold);
    
    @Query("{'happiness': {$lt: ?0}}")
    List<Pet> findUnhappyPets(Integer happinessThreshold);
    
    @Query("{'health': {$lt: ?0}}")
    List<Pet> findSickPets(Integer healthThreshold);
    
    @Query("{'lastFed': {$lt: ?0}}")
    List<Pet> findPetsNotFedSince(LocalDateTime dateTime);
    
    @Query(value = "{}", sort = "{'level': -1, 'experience': -1}")
    Page<Pet> findTopPetsByLevel(Pageable pageable);
    
    Long countByOwnerId(String ownerId);
    
    Optional<Pet> findByOwnerIdAndName(String ownerId, String name);
    
    boolean existsByOwnerIdAndName(String ownerId, String name);
}
