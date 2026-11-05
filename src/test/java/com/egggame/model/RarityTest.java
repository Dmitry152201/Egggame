package com.egggame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты enum редкости.
 * Проверяют корректность отображаемых имен и шансов выпадения.
 */
class RarityTest {

    /**
     * Проверяет, что у редкости правильное отображаемое имя.
     * Ожидаемые значения заданы константами прямо в тесте (Слайд 16).
     */
    @Test
    void testGetDisplayName_returnsCorrectString() {
        assertEquals("⚪ Обычный", Rarity.COMMON.getDisplayName());
        assertEquals("🔵 Редкий", Rarity.RARE.getDisplayName());
        assertEquals("🟣 Эпический", Rarity.EPIC.getDisplayName());
        assertEquals("🟡 Легендарный", Rarity.LEGENDARY.getDisplayName());
    }

    /**
     * Проверяет корректность шансов выпадения.
     */
    @Test
    void testGetDropChancePercent_returnsCorrectValues() {
        assertEquals(60, Rarity.COMMON.getDropChancePercent());
        assertEquals(25, Rarity.RARE.getDropChancePercent());
        assertEquals(10, Rarity.EPIC.getDropChancePercent());
        assertEquals(5, Rarity.LEGENDARY.getDropChancePercent());
    }
}