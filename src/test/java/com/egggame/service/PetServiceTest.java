package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {
    private PetService petService;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        petService = new PetService(new SpeciesData(), petStorage, config);
        player = new Player(111L, "TestPlayer");
    }

    @Test
    void createPet_worksCorrectly() {
        Pet pet = petService.createPet(player, "mouse");
        assertEquals(1, pet.getLevel());
    }

    @Test
    void updatePetStats_timePassed_reducesSatiety() {
        Pet pet = petService.createPet(player, "mouse");
        pet.setLastUpdatedAt(LocalDateTime.now().minusHours(10));
        petService.updatePetStats(pet);
        assertEquals(0, pet.getSatiety());
    }

    @Test
    void addExperience_levelsUpPet() {
        Pet pet = petService.createPet(player, "mouse");
        String log = petService.addExperience(pet, 120);
        assertEquals(2, pet.getLevel());
        assertEquals(20, pet.getExperience());
        assertTrue(log.contains("Уровень повышен"));
    }
}