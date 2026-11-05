package com.egggame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты модели вида питомца.
 */
class SpeciesTest {

    private Species species;

    /**
     * Подготовка общих данных перед каждым тестом (Слайд 23).
     */
    @BeforeEach
    void setUp() {
        species = new Species("mouse", "Сырный Мышонок", Rarity.COMMON,
                "Обожает сыр.", "pets/mouse.jpg");
    }

    /**
     * Проверяет, что конструктор правильно сохраняет все переданные значения.
     */
    @Test
    void testSpeciesCreation_storesCorrectValues() {
        assertEquals("mouse", species.getId());
        assertEquals("Сырный Мышонок", species.getName());
        assertEquals(Rarity.COMMON, species.getRarity());
        assertEquals("Обожает сыр.", species.getDescription());
        assertEquals("pets/mouse.jpg", species.getImagePath());
    }
}