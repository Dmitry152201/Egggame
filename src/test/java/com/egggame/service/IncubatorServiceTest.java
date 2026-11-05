package com.egggame.service;

import com.egggame.config.Config;
import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты сервиса инкубатора.
 */
class IncubatorServiceTest {

    private IncubatorService incubatorService;
    private Player player;

    @BeforeEach
    void setUp() throws IOException {
        Config config = new Config();
        SpeciesData speciesData = new SpeciesData();
        PetStorage petStorage = new PetStorage();
        PetService petService = new PetService(speciesData, petStorage);
        incubatorService = new IncubatorService(config, petService);
        player = new Player(123456789L, "TestPlayer");
    }

    /**
     * Проверяет успешную закладку яйца и списание его из инвентаря.
     */
    @Test
    void testPutEgg_success_spendsEggAndSetsTimer() {
        player.addEgg();
        IncubatorService.IncubatorResult result = incubatorService.putEgg(player);

        assertTrue(result.isSuccess());
        assertEquals(0, player.getEggs());
        assertNotNull(player.getHatchReadyAt());
        assertFalse(player.isIncubatorEmpty());
    }

    /**
     * Проверяет противоположный случай: попытку положить яйцо без яиц.
     */
    @Test
    void testPutEgg_noEggs_fails() {
        IncubatorService.IncubatorResult result = incubatorService.putEgg(player);

        assertFalse(result.isSuccess());
        assertEquals(" У тебя нет яиц. Получи новое командой /daily.", result.getMessage());
    }

    /**
     * Проверяет, что нельзя забрать питомца до истечения таймера.
     */
    @Test
    void testHatch_tooEarly_fails() {
        player.addEgg();
        incubatorService.putEgg(player);

        IncubatorService.IncubatorResult result = incubatorService.hatch(player);

        assertFalse(result.isSuccess());
        assertEquals("⏳ Яйцо ещё не вылупилось. Подожди немного.", result.getMessage());
    }

    /**
     * Проверяет успешное вылупление, когда время пришло.
     */
    @Test
    void testHatch_readyTimePassed_success() {
        player.addEgg();
        incubatorService.putEgg(player);

        // Искусственно сдвигаем время готовности в прошлое
        player.setHatchReadyAt(LocalDateTime.now().minusSeconds(1));

        IncubatorService.IncubatorResult result = incubatorService.hatch(player);

        assertTrue(result.isSuccess());
        assertNotNull(result.getSpecies());
        assertTrue(player.isIncubatorEmpty()); // Инкубатор очистился
    }
}