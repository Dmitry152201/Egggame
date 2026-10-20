package com.egggame.model;

/**
 * Редкость питомца.
 * Определяет базовый шанс выпадения из яйца и отображаемое
 * имя с эмодзи для сообщений бота.
 */
public enum Rarity {

    /** Обычный питомец, самый частый дроп */
    COMMON("⚪ Обычный", 60),

    /** Редкий питомец */
    RARE("🔵 Редкий", 25),

    /** Эпический питомец */
    EPIC("🟣 Эпический", 10),

    /** Легендарный питомец, самый редкий дроп */
    LEGENDARY("🟡 Легендарный", 5);

    /** Отображаемое имя редкости для игрока */
    private final String displayName;

    /** Базовый шанс выпадения в процентах */
    private final int dropChancePercent;

    /**
     * Создаёт уровень редкости.
     *
     * @param displayName       имя редкости для игрока
     * @param dropChancePercent базовый шанс выпадения в процентах
     */
    Rarity(String displayName, int dropChancePercent) {
        this.displayName = displayName;
        this.dropChancePercent = dropChancePercent;
    }

    /**
     * Возвращает имя редкости для показа игроку.
     *
     * @return имя с эмодзи, например "🔵 Редкий"
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Возвращает базовый шанс выпадения редкости в процентах.
     *
     * @return шанс в процентах (0..100)
     */
    public int getDropChancePercent() {
        return dropChancePercent;
    }
}