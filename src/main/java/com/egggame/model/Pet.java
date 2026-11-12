package com.egggame.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pet {
    // ... (остальные поля остаются без изменений) ...
    private final String id;
    private final long ownerId;
    private final String speciesId;
    private String name;
    private int level;
    private final LocalDateTime obtainedAt;
    private int satiety;
    private int happiness;
    private final List<String> traits;
    private LocalDateTime lastPatAt;
    private LocalDateTime lastUpdatedAt;
    private int experience;
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;

    // НОВОЕ ПОЛЕ
    private LocalDateTime arenaEndTime;
    private int arenaHours; // Сколько часов было запрошено

    public Pet(String id, long ownerId, Species species, String name, LocalDateTime obtainedAt, List<String> traits) {
        // ... (инициализация остается прежней) ...
        this.id = id;
        this.ownerId = ownerId;
        this.speciesId = species.getId();
        this.name = name;
        this.level = 1;
        this.obtainedAt = obtainedAt;
        this.traits = new ArrayList<>(traits);

        this.satiety = 50;
        this.happiness = 50;
        this.lastUpdatedAt = LocalDateTime.now();

        this.experience = 0;
        recalculateStats(species);
        this.currentHp = this.maxHp;
    }

    public void recalculateStats(Species species) {
        this.maxHp = species.getBaseHp() + ((level - 1) * 10);
        this.attack = species.getBaseAttack() + ((level - 1) * 5);
        this.defense = species.getBaseDefense() + ((level - 1) * 2);
    }

    public boolean isOnArena() {
        return arenaEndTime != null && LocalDateTime.now().isBefore(arenaEndTime);
    }

    // ... (все старые геттеры/сеттеры остаются) ...
    public String getId() { return id; }
    public long getOwnerId() { return ownerId; }
    public String getSpeciesId() { return speciesId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public LocalDateTime getObtainedAt() { return obtainedAt; }
    public int getSatiety() { return satiety; }
    public void setSatiety(int satiety) { this.satiety = Math.max(0, Math.min(100, satiety)); }
    public int getHappiness() { return happiness; }
    public void setHappiness(int happiness) { this.happiness = Math.max(0, Math.min(100, happiness)); }
    public List<String> getTraits() { return traits; }
    public LocalDateTime getLastPatAt() { return lastPatAt; }
    public void setLastPatAt(LocalDateTime lastPatAt) { this.lastPatAt = lastPatAt; }
    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public void setCurrentHp(int currentHp) { this.currentHp = Math.max(0, Math.min(maxHp, currentHp)); }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public LocalDateTime getArenaEndTime() { return arenaEndTime; }
    public void setArenaEndTime(LocalDateTime arenaEndTime) { this.arenaEndTime = arenaEndTime; }

    // Новые геттер и сеттер для часов
    public int getArenaHours() { return arenaHours; }
    public void setArenaHours(int arenaHours) { this.arenaHours = arenaHours; }
}