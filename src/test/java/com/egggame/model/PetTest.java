package com.egggame.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты модели питомца.
 */
class PetTest {

    /**
     * Проверяет корректную инициализацию и защиту границ статов (0-100).
     */
    @Test
    void testPet_setStats_cannotExceedBounds() {
        Pet pet = new Pet("mouse_1", 123456789L, "mouse", 1, LocalDateTime.now(), List.of("Соня"));

        pet.setSatiety(150);
        assertEquals(100, pet.getSatiety()); // Обрезка до 100

        pet.setHappiness(-20);
        assertEquals(0, pet.getHappiness()); // Обрезка до 0
    }
}