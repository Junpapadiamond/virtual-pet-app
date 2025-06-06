package com.snowleopard.virtual_pet.repository;

import com.snowleopard.virtual_pet.entity.Activity;
import com.snowleopard.virtual_pet.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
    
    List<Activity> findByPetId(String petId);
    
    List<Activity> findByUserId(String userId);
    
    List<Activity> findByPetIdOrderByTimestampDesc(String petId);
    
    List<Activity> findByUserIdOrderByTimestampDesc(String userId);
    
    List<Activity> findByType(ActivityType type);
    
    @Query("{'petId': ?0, 'type': ?1}")
    List<Activity> findByPetIdAndType(String petId, ActivityType type);
    
    @Query("{'timestamp': {$gte: ?0, $lte: ?1}}")
    List<Activity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1}}")
    List<Activity> findByUserIdAndTimestampAfter(String userId, LocalDateTime timestamp);
    
    Long countByPetId(String petId);
    
    Long countByUserIdAndType(String userId, ActivityType type);
    
    Page<Activity> findByPetIdOrderByTimestampDesc(String petId, Pageable pageable);
}
