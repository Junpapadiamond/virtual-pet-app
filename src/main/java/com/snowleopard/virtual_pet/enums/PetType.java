package com.snowleopard.virtual_pet.enums;

import lombok.Getter;

@Getter
public enum PetType {
    DOG("Dog", "Loyal and playful companion", 0.8, 0.7, 0.9),
    CAT("Cat", "Independent and mysterious", 0.6, 0.9, 0.5),
    DRAGON("Dragon", "Legendary and powerful", 0.9, 0.8, 0.8),
    ROBOT("Robot Pet", "High-tech digital companion", 0.7, 0.6, 0.9),
    FAIRY("Fairy", "Magical and ethereal", 0.8, 0.9, 0.7);

    private final String displayName;
    private final String description;
    private final double loyaltyFactor;
    private final double intelligenceFactor;
    private final double energyFactor;

    PetType(String displayName, String description, double loyaltyFactor, double intelligenceFactor, double energyFactor) {
        this.displayName = displayName;
        this.description = description;
        this.loyaltyFactor = loyaltyFactor;
        this.intelligenceFactor = intelligenceFactor;
        this.energyFactor = energyFactor;
    }
}
