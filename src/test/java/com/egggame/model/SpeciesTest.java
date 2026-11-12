package com.egggame.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SpeciesTest {
    @Test
    void species_creation_correct() {
        Species species = new Species(
                "mouse", "Сырный Мышонок", Rarity.COMMON, "Обожает сыр.", "pets/mouse.jpg",
                List.of("Любит сыр"), 30, 5, 1, null, null
        );
        assertEquals("mouse", species.getId());
        assertEquals(30, species.getBaseHp());
        assertFalse(species.hasPassive());
    }
}