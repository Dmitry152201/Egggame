package com.egggame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты enum редкости.
 */
class RarityTest {

    @Test
    void rarity_values_correct() {
        assertEquals(4, Rarity.values().length);
        assertEquals(Rarity.COMMON, Rarity.valueOf("COMMON"));
    }

    @Test
    void rarity_displayName_correct() {
        assertEquals("⚪ Обычный", Rarity.COMMON.getDisplayName());
        assertEquals("🔵 Редкий", Rarity.RARE.getDisplayName());
        assertEquals("🟣 Эпический", Rarity.EPIC.getDisplayName());
        assertEquals("🟡 Легендарный", Rarity.LEGENDARY.getDisplayName());
    }

    @Test
    void rarity_dropChance_correct() {
        assertEquals(60, Rarity.COMMON.getDropChancePercent());
        assertEquals(25, Rarity.RARE.getDropChancePercent());
        assertEquals(10, Rarity.EPIC.getDropChancePercent());
        assertEquals(5, Rarity.LEGENDARY.getDropChancePercent());
    }

    @Test
    void rarity_baseSellPrice_correct() {
        assertEquals(10, Rarity.COMMON.getBaseSellPrice());
        assertEquals(30, Rarity.RARE.getBaseSellPrice());
        assertEquals(100, Rarity.EPIC.getBaseSellPrice());
        assertEquals(300, Rarity.LEGENDARY.getBaseSellPrice());
    }
}