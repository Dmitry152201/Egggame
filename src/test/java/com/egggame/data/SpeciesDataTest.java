package com.egggame.data;

import com.egggame.model.Species;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты каталога видов.
 */
class SpeciesDataTest {

    /**
     * Проверяет, что каталог не пустой и содержит нужное количество видов.
     */
    @Test
    void testGetAllSpecies_returnsAll10Species() {
        SpeciesData data = new SpeciesData();
        assertEquals(10, data.getTotalCount());
    }

    /**
     * Проверяет поиск по ID.
     */
    @Test
    void testGetById_existingId_returnsSpecies() {
        SpeciesData data = new SpeciesData();
        Optional<Species> mouse = data.getById("mouse");

        assertTrue(mouse.isPresent());
        assertEquals("Сырный Мышонок", mouse.get().getName());
    }

    /**
     * Проверяет противоположный случай: поиск несуществующего ID.
     */
    @Test
    void testGetById_unknownId_returnsEmpty() {
        SpeciesData data = new SpeciesData();
        Optional<Species> unknown = data.getById("unknown");
        assertFalse(unknown.isPresent());
    }
}