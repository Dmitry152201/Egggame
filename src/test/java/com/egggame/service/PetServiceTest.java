package com.egggame.service;

import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.model.Species;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты сервиса питомцев.
 */
class PetServiceTest {

    private PetService petService;
    private Player player;

    @BeforeEach
    void setUp() throws IOException {
        SpeciesData speciesData = new SpeciesData();
        PetStorage petStorage = new PetStorage();
        petService = new PetService(speciesData, petStorage);
        player = new Player(123456789L, "TestPlayer");
    }

    @Test
    void chooseRandomSpecies_newPlayer_returnsSpecies() {
        Species species = petService.chooseRandomSpecies(player);
        assertNotNull(species);
    }

    @Test
    void extractSpeciesId_validId_returnsSpeciesId() {
        assertEquals("mouse", petService.extractSpeciesId("mouse_1"));
    }

    @Test
    void extractSpeciesId_noUnderscore_returnsFullId() {
        assertEquals("mouse", petService.extractSpeciesId("mouse"));
    }
}