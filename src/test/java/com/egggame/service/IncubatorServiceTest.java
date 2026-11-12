package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class IncubatorServiceTest {
    private IncubatorService incubatorService;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PetService petService = new PetService(new SpeciesData(), petStorage, config);
        incubatorService = new IncubatorService(config, petService);
        player = new Player(111L, "TestPlayer");
    }

    @Test
    void putEgg_emptyIncubator_success() {
        player.addEgg();
        assertTrue(incubatorService.putEgg(player).success());
    }

    @Test
    void putEgg_noEggs_fails() {
        assertFalse(incubatorService.putEgg(player).success());
    }
}