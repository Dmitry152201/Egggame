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

class ArenaServiceTest {
    private ArenaService arenaService;
    private PetService petService;
    private Pet pet;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        petService = new PetService(new SpeciesData(), petStorage, config);
        arenaService = new ArenaService(petService, config);
        Player player = new Player(111L, "TestPlayer");
        pet = petService.createPet(player, "mouse");
    }

    @Test
    void sendToArena_setsEndTime() {
        ArenaService.ArenaResult result = arenaService.sendToArena(pet, 2);
        assertTrue(result.success());
        assertNotNull(pet.getArenaEndTime());
        assertTrue(pet.isOnArena());
        assertEquals(2, pet.getArenaHours());
    }

    @Test
    void claim_tooEarly_fails() {
        arenaService.sendToArena(pet, 2);
        ArenaService.ArenaResult result = arenaService.claim(pet);
        assertFalse(result.success());
        assertTrue(result.message().contains("еще не окончена"));
    }

    @Test
    void claim_afterTime_returnsXp() {
        arenaService.sendToArena(pet, 2);
        pet.setArenaEndTime(LocalDateTime.now().minusMinutes(1));
        ArenaService.ArenaResult result = arenaService.claim(pet);
        assertTrue(result.success());
        assertTrue(result.message().contains("получил 20 XP"));
        assertNull(pet.getArenaEndTime());
        assertEquals(0, pet.getArenaHours());
    }
}