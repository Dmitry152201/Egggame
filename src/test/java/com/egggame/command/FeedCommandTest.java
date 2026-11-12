package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FeedCommandTest {
    private FeedCommand feedCommand;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PlayerStorage playerStorage = new PlayerStorage(tempDir.resolve("players.json").toString());

        PetService petService = new PetService(new SpeciesData(), petStorage, config);
        PlayerService playerService = new PlayerService(playerStorage);
        feedCommand = new FeedCommand(petService, playerService);

        player = new Player(111L, "TestUser");
        player.addEgg();
        petService.createPet(player, "mouse");
    }

    @Test
    void execute_hasEgg_feedPet() {
        CommandResponse response = feedCommand.execute(player, new String[]{"1"});
        assertEquals(0, player.getEggs());
        assertTrue(response.text().contains("Покормил"));
    }

    @Test
    void execute_noEggs_fails() {
        player.setEggs(0);
        CommandResponse response = feedCommand.execute(player, new String[]{"1"});
        assertTrue(response.text().contains("Нет яиц"));
    }
}