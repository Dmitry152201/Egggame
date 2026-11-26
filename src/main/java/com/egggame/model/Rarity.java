package com.egggame.model;

/**
 * Редкость питомца.
 * Определяет базовый шанс выпадения из яйца и отображаемое
 * имя с эмодзи для сообщений бота.
 */
public enum Rarity {

    /**
     * Обычный питомец, самый частый дроп
     */
    COMMON("⚪ Обычный", 60, 10),

    /**
     * Редкий питомец
     */
    RARE("🔵 Редкий", 25, 30),

    /**
     * Эпический питомец
     */
    EPIC("🟣 Эпический", 10, 100),

    /**
     * Легендарный питомец, самый редкий дроп
     */
    LEGENDARY("🟡 Легендарный", 5, 300);

    /**
     * Отображаемое имя редкости для игрока
     */
    private final String displayName;

    /**
     * Базовый шанс выпадения в процентах
     */
    private final int dropChancePercent;

    private final int baseSellPrice;

    /**
     * Создаёт уровень редкости.
     *
     * @param displayName       имя редкости для игрока
     * @param dropChancePercent базовый шанс выпадения в процентах
     */
    Rarity(String displayName, int dropChancePercent, int baseSellPrice) {
        this.displayName = displayName;
        this.dropChancePercent = dropChancePercent;
        this.baseSellPrice = baseSellPrice;
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

    public int getBaseSellPrice() { return baseSellPrice; }
}