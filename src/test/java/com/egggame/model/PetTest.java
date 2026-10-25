package com.egggame.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты модели питомца.
 */
class PetTest {

    @Test
    void pet_creation_correct() {
        LocalDateTime now = LocalDateTime.now();
        Pet pet = new Pet("mouse_1", 123456789L, "mouse", 1, now);

        assertEquals("mouse_1", pet.getId());
        assertEquals(123456789L, pet.getOwnerId());
        assertEquals("mouse", pet.getSpeciesId());
        assertEquals(1, pet.getLevel());
        assertEquals(now, pet.getObtainedAt());
    }

    @Test
    void pet_setLevel_updatesLevel() {
        Pet pet = new Pet("mouse_1", 123456789L, "mouse", 1, LocalDateTime.now());
        pet.setLevel(5);
        assertEquals(5, pet.getLevel());
    }
}