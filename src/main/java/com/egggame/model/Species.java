package com.egggame.model;

import java.util.List;

/**
 * Шаблон вида питомца. Содержит базовые боевые характеристики и пассивку.
 */
public class Species {
    private final String id;
    private final String name;
    private final Rarity rarity;
    private final String description;
    private final String imagePath;
    private final List<String> availableTraits;

    // Боевые статы
    private final int baseHp;
    private final int baseAttack;
    private final int baseDefense;

    // Пассивная способность
    private final String passiveName;
    private final String passiveDescription;

    public Species(String id, String name, Rarity rarity, String description, String imagePath,
                   List<String> availableTraits, int baseHp, int baseAttack, int baseDefense,
                   String passiveName, String passiveDescription) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.imagePath = imagePath;
        this.availableTraits = availableTraits;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.passiveName = passiveName;
        this.passiveDescription = passiveDescription;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Rarity getRarity() { return rarity; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public List<String> getAvailableTraits() { return availableTraits; }
    public int getBaseHp() { return baseHp; }
    public int getBaseAttack() { return baseAttack; }
    public int getBaseDefense() { return baseDefense; }
    public String getPassiveName() { return passiveName; }
    public String getPassiveDescription() { return passiveDescription; }

    /** Проверяет наличие пассивки */
    public boolean hasPassive() { return passiveName != null && !passiveName.isEmpty(); }
}