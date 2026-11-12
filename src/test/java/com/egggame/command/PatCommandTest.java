package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class PatCommandTest {
    private PatCommand patCommand;
    private Player player;
    private Pet pet;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PetService petService = new PetService(new SpeciesData(), petStorage, config);
        patCommand = new PatCommand(petService, config);

        player = new Player(111L, "TestUser");
        pet = petService.createPet(player, "mouse");
    }

    @Test
    void execute_patPet_increasesHappiness() {
        CommandResponse response = patCommand.execute(player, new String[]{pet.getId()});
        assertTrue(response.text().toLowerCase().contains("погладил"));
    }
}