package com.snowleopard.virtual_pet.repository;

import com.snowleopard.virtual_pet.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("{'playerLevel': {$gte: ?0}}")
    List<User> findByPlayerLevelGreaterThanEqual(Integer level);
    
    @Query("{'lastLoginDate': {$gte: ?0}}")
    List<User> findActiveUsersSince(LocalDateTime date);
    
    // Leaderboard queries
    @Query(value = "{}", sort = "{'playerLevel': -1, 'totalExperience': -1}")
    Page<User> findTopUsersByLevel(Pageable pageable);
    
    @Query(value = "{}", sort = "{'totalCoins': -1}")
    Page<User> findRichestUsers(Pageable pageable);
    
    Long countByPlayerLevel(Integer level);
}
