package com.egggame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты модели вида питомца.
 */
class SpeciesTest {

    @Test
    void species_creation_correct() {
        Species species = new Species("mouse", "Сырный Мышонок", Rarity.COMMON, "Обожает сыр.", "pets/mouse.jpg");

        assertEquals("mouse", species.getId());
        assertEquals("Сырный Мышонок", species.getName());
        assertEquals(Rarity.COMMON, species.getRarity());
        assertEquals("Обожает сыр.", species.getDescription());
        assertEquals("pets/mouse.jpg", species.getImagePath());
    }
}