package com.snowleopard.virtual_pet.service;

import com.snowleopard.virtual_pet.entity.Activity;
import com.snowleopard.virtual_pet.enums.ActivityType;
import com.snowleopard.virtual_pet.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public List<Activity> getActivitiesByPetId(String petId) {
        return activityRepository.findByPetIdOrderByTimestampDesc(petId);
    }

    public List<Activity> getActivitiesByUserId(String userId) {
        return activityRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public Page<Activity> getActivitiesByPetId(String petId, Pageable pageable) {
        return activityRepository.findByPetIdOrderByTimestampDesc(petId, pageable);
    }

    public List<Activity> getActivitiesInTimeRange(LocalDateTime start, LocalDateTime end) {
        return activityRepository.findByTimestampBetween(start, end);
    }

    public Long getActivityCountByPet(String petId) {
        return activityRepository.countByPetId(petId);
    }

    public Long getActivityCountByUserAndType(String userId, ActivityType type) {
        return activityRepository.countByUserIdAndType(userId, type);
    }
}
