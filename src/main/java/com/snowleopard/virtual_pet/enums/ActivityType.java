package com.snowleopard.virtual_pet.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    FEED("Feed", "Give food to your pet", 0, 5, 0, 2),
    PLAY("Play", "Play games with your pet", 15, 0, 10, 5),
    EXERCISE("Exercise", "Take your pet for exercise", 20, 0, 5, 8),
    SLEEP("Sleep", "Let your pet rest", -10, 0, 0, -15),
    TRAIN("Train", "Teach your pet new skills", 25, 0, 5, 10),
    GROOMING("Grooming", "Clean and groom your pet", 5, 0, 8, 3),
    MEDICAL("Medical", "Take care of pet's health", 0, 0, 15, 0);

    private final String displayName;
    private final String description;
    private final int energyCost;
    private final int hungerEffect;
    private final int happinessGain;
    private final int experienceGain;

    ActivityType(String displayName, String description, int energyCost, int hungerEffect, int happinessGain, int experienceGain) {
        this.displayName = displayName;
        this.description = description;
        this.energyCost = energyCost;
        this.hungerEffect = hungerEffect;
        this.happinessGain = happinessGain;
        this.experienceGain = experienceGain;
    }
}
