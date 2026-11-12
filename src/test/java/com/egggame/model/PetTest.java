package com.egggame.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PetTest {
    @Test
    void pet_creation_correct() {
        // 11 параметров для Species
        Species species = new Species(
                "mouse", "Мышь", Rarity.COMMON, "Описание", "path", List.of(),
                30, 5, 1, null, null
        );
        Pet pet = new Pet("mouse_1", 123456789L, species, "Имя", LocalDateTime.now(), List.of("Черта"));

        assertEquals("mouse_1", pet.getId());
        assertEquals("Имя", pet.getName());
        assertEquals(1, pet.getLevel());
        assertEquals(50, pet.getSatiety());
        assertEquals(30, pet.getMaxHp());
        assertEquals(5, pet.getAttack());
    }

    @Test
    void pet_recalculateStats_increasesCorrectly() {
        Species species = new Species(
                "mouse", "Мышь", Rarity.COMMON, "Описание", "path", List.of(),
                30, 5, 1, null, null
        );
        Pet pet = new Pet("mouse_1", 123456789L, species, "Имя", LocalDateTime.now(), List.of());

        pet.setLevel(2);
        pet.recalculateStats(species);

        assertEquals(40, pet.getMaxHp()); // 30 + 10
        assertEquals(10, pet.getAttack()); // 5 + 5
        assertEquals(3, pet.getDefense()); // 1 + 2
    }
}