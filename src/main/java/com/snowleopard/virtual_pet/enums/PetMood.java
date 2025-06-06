package com.snowleopard.virtual_pet.enums;

import lombok.Getter;

@Getter
public enum PetMood {
    ECSTATIC("Ecstatic", "😍", 90, 100),
    HAPPY("Happy", "😊", 75, 89),      // Fixed: was 70-89
    CONTENT("Content", "😌", 50, 74),   // Fixed: was 50-69
    NEUTRAL("Neutral", "😐", 25, 49),   // Fixed: was 30-49
    SAD("Sad", "😢", 10, 24),          // Fixed: was 10-29
    DEPRESSED("Depressed", "😭", 0, 9);

    private final String name;
    private final String emoji;
    private final int minScore;
    private final int maxScore;

    PetMood(String name, String emoji, int minScore, int maxScore) {
        this.name = name;
        this.emoji = emoji;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public static PetMood fromScore(int score) {
        for (PetMood mood : values()) {
            if (score >= mood.minScore && score <= mood.maxScore) {
                return mood;
            }
        }
        return NEUTRAL;
    }
}
