package com.egggame.command;

import com.egggame.config.Config;
import com.egggame.data.SpeciesData;
import com.egggame.service.EggService;
import com.egggame.service.IncubatorService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты обработчика команд.
 */
class CommandHandlerTest {

    private CommandHandler commandHandler;
    private PlayerStorage playerStorage;

    @BeforeEach
    void setUp() throws IOException {
        Config config = new Config();
        SpeciesData speciesData = new SpeciesData();
        playerStorage = new PlayerStorage();
        PetStorage petStorage = new PetStorage();

        PlayerService playerService = new PlayerService(playerStorage);
        EggService eggService = new EggService(config);
        PetService petService = new PetService(speciesData, petStorage);
        IncubatorService incubatorService = new IncubatorService(config, petService);

        commandHandler = new CommandHandler(playerService, eggService, incubatorService, petService, speciesData);
    }

    @Test
    void handleCommand_start_returnsGreeting() {
        CommandResponse response = commandHandler.handleCommand(111L, "/start", "TestUser");
        assertNotNull(response.getText());
        assertTrue(response.getText().contains("EGGGAME"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void handleCommand_help_returnsCommandList() {
        CommandResponse response = commandHandler.handleCommand(111L, "/help", "TestUser");
        assertTrue(response.getText().contains("/daily"));
        assertTrue(response.getText().contains("/incubate"));
    }

    @Test
    void handleCommand_daily_grantsEgg() {
        CommandResponse response = commandHandler.handleCommand(222L, "/daily", "TestUser");
        assertTrue(response.getText().contains("яйцо"));
    }

    @Test
    void handleCommand_profile_returnsStats() {
        commandHandler.handleCommand(333L, "/daily", "TestUser");
        CommandResponse response = commandHandler.handleCommand(333L, "/profile", "TestUser");
        assertTrue(response.getText().contains("Яиц: 1"));
    }

    @Test
    void handleCommand_unknownCommand_returnsError() {
        CommandResponse response = commandHandler.handleCommand(444L, "/unknown", "TestUser");
        assertTrue(response.getText().contains("Неизвестная команда"));
    }
}