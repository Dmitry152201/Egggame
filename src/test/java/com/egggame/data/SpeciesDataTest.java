package com.egggame.data;

import com.egggame.model.Species;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты каталога видов.
 */
class SpeciesDataTest {

    @Test
    void speciesData_containsAllSpecies() {
        SpeciesData data = new SpeciesData();
        assertEquals(10, data.getTotalCount());
        assertEquals(10, data.getAllSpecies().size());
    }

    @Test
    void speciesData_getById_returnsCorrectSpecies() {
        SpeciesData data = new SpeciesData();
        Optional<Species> mouse = data.getById("mouse");
        assertTrue(mouse.isPresent());
        assertEquals("Сырный Мышонок", mouse.get().getName());
    }

    @Test
    void speciesData_getById_returnsEmptyForUnknown() {
        SpeciesData data = new SpeciesData();
        Optional<Species> unknown = data.getById("unknown");
        assertFalse(unknown.isPresent());
    }
}