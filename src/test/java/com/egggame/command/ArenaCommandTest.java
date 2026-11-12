package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.service.ArenaService;
import com.egggame.service.PetService;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ArenaCommandTest {
    private ArenaCommand arenaCommand;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PetService petService = new PetService(new SpeciesData(), petStorage, config);
        ArenaService arenaService = new ArenaService(petService, config);
        arenaCommand = new ArenaCommand(petService, arenaService);

        player = new Player(111L, "TestUser");
        petService.createPet(player, "mouse");
    }

    @Test
    void execute_sendToArena_success() {
        CommandResponse response = arenaCommand.execute(player, new String[]{"send", "1", "2"});
        assertTrue(response.text().contains("отправил"));
    }

    @Test
    void execute_unknownSubcommand_returnsError() {
        CommandResponse response = arenaCommand.execute(player, new String[]{"invalid"});
        assertTrue(response.text().contains("Неизвестная подкоманда"));
    }
}