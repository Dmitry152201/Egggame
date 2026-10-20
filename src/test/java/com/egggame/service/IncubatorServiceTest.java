package com.egggame.service;

import com.egggame.Config;
import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты сервиса инкубатора.
 */
class IncubatorServiceTest {

    private IncubatorService incubatorService;
    private Player player;

    @BeforeEach
    void setUp() {
        Config config = new Config();
        SpeciesData speciesData = new SpeciesData();
        PetStorage petStorage = new PetStorage();
        PetService petService = new PetService(speciesData, petStorage);
        incubatorService = new IncubatorService(config, petService);
        player = new Player(123456789L, "TestPlayer");
    }

    @Test
    void putEgg_emptyIncubator_success() {
        player.addEgg();
        IncubatorService.IncubatorResult result = incubatorService.putEgg(player);
        assertTrue(result.isSuccess());
    }

    @Test
    void putEgg_occupiedIncubator_fails() {
        player.addEgg();
        incubatorService.putEgg(player);
        IncubatorService.IncubatorResult result = incubatorService.putEgg(player);
        assertFalse(result.isSuccess());
    }

    @Test
    void putEgg_noEggs_fails() {
        IncubatorService.IncubatorResult result = incubatorService.putEgg(player);
        assertFalse(result.isSuccess());
    }
}