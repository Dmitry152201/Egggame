package com.egggame.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Конкретный экземпляр питомца у игрока.
 * В отличие от Species (шаблона вида), Pet — это живой питомец
 * с уникальным id, уровнем, потребностями и эмоциями.
 */
public class Pet {

    /** Уникальный идентификатор питомца (формат: "вид_номер") */
    private final String id;

    /** Telegram ID владельца */
    private final long ownerId;

    /** ID вида питомца (ссылка на Species) */
    private final String speciesId;

    /** Уровень питомца */
    private int level;

    /** Дата и время получения питомца */
    private final LocalDateTime obtainedAt;

    // ========= ПОЛЯ ДЛЯ ЗАДАЧИ 2 (ТАМАГОЧИ) =========

    /** Сытость питомца от 0 до 100 */
    private int satiety;

    /** Счастье питомца от 0 до 100 */
    private int happiness;

    /** Врожденные черты характера */
    private List<String> traits;

    /** Время последнего поглаживания (для кулдауна /pat) */
    private LocalDateTime lastPatAt;

    /** Время последнего пересчета статов (для фонового голода) */
    private LocalDateTime lastUpdatedAt;

    /**
     * Создаёт экземпляр питомца.
     */
    public Pet(String id, long ownerId, String speciesId, int level,
               LocalDateTime obtainedAt, List<String> traits) {
        this.id = id;
        this.ownerId = ownerId;
        this.speciesId = speciesId;
        this.level = level;
        this.obtainedAt = obtainedAt;
        this.traits = traits != null ? new ArrayList<>(traits) : new ArrayList<>();

        // Базовые показатели при рождении
        this.satiety = 50;
        this.happiness = 50;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public long getOwnerId() { return ownerId; }
    public String getSpeciesId() { return speciesId; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public LocalDateTime getObtainedAt() { return obtainedAt; }

    public int getSatiety() { return satiety; }
    public void setSatiety(int satiety) { this.satiety = Math.max(0, Math.min(100, satiety)); }

    public int getHappiness() { return happiness; }
    public void setHappiness(int happiness) { this.happiness = Math.max(0, Math.min(100, happiness)); }

    /**
     * Возвращает список черт. Защищен от null для старых сохранений.
     */
    public List<String> getTraits() {
        if (traits == null) {
            traits = new ArrayList<>();
        }
        return traits;
    }

    public LocalDateTime getLastPatAt() { return lastPatAt; }
    public void setLastPatAt(LocalDateTime lastPatAt) { this.lastPatAt = lastPatAt; }

    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
}