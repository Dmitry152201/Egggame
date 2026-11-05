package com.egggame.service;

import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты сервиса питомцев.
 */
class PetServiceTest {

    private PetService petService;
    private Player player;

    /**
     * Инициализация. Используем уникальный ID игрока,
     * чтобы тест не зависел от старых данных в JSON-файле.
     */
    @BeforeEach
    void setUp() throws IOException {
        SpeciesData speciesData = new SpeciesData();
        PetStorage petStorage = new PetStorage();
        petService = new PetService(speciesData, petStorage);
        // Генерируем уникальный ID для каждого запуска теста
        player = new Player(System.currentTimeMillis(), "TestPlayer");
    }

    /**
     * Тестирует фоновое снижение сытости.
     */
    @Test
    void testUpdatePetStats_tenHoursPassed_reducesSatietyAndHappiness() {
        Pet pet = petService.createPet(player, "mouse");

        pet.setLastUpdatedAt(LocalDateTime.now().minusHours(10));
        petService.updatePetStats(pet);

        assertEquals(0, pet.getSatiety());
        assertEquals(30, pet.getHappiness());
    }

    /**
     * Тестирует поиск по номеру и по имени.
     */
    @Test
    void testFindPet_byNumberAndName_returnsCorrectPet() {
        Pet pet = petService.createPet(player, "mouse");

        // Так как у игрока только 1 питомец (созданный в этом тесте), поиск по "1" сработает верно
        Pet foundByNumber = petService.findPet(player, "1");
        Pet foundByName = petService.findPet(player, "Сырный Мышонок");

        assertNotNull(foundByNumber);
        assertEquals(pet.getId(), foundByNumber.getId());

        assertNotNull(foundByName);
        assertEquals(pet.getId(), foundByName.getId());
    }

    /**
     * Тестирует, что при поиске несуществующего питомца возвращается null.
     */
    @Test
    void testFindPet_unknownQuery_returnsNull() {
        assertNull(petService.findPet(player, "99"));
        assertNull(petService.findPet(player, "Неизвестный Вид"));
    }
}