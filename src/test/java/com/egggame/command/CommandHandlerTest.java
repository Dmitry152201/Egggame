package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.service.*;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {
    private CommandHandler commandHandler;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PlayerStorage playerStorage = new PlayerStorage(tempDir.resolve("players.json").toString());
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());

        PlayerService playerService = new PlayerService(playerStorage);
        EggService eggService = new EggService(config);
        PetService petService = new PetService(new SpeciesData(), petStorage, config);
        IncubatorService incubatorService = new IncubatorService(config, petService);
        CombatService combatService = new CombatService(petService);
        ArenaService arenaService = new ArenaService(petService, config);

        List<Command> commands = List.of(
                new StartCommand(playerService),
                new HelpCommand(),
                new DailyCommand(eggService, playerService),
                new FightCommand(petService, combatService),
                new ArenaCommand(petService, arenaService)
        );
        commandHandler = new CommandHandler(playerService, commands);
    }

    @Test
    void handleCommand_start_routedCorrectly() {
        CommandResponse response = commandHandler.handleCommand(111L, "/start", "User");
        // Проверяем, что команда /start отработала и вернула правильный текст
        assertTrue(response.text().contains("EGGGAME"));
    }

    @Test
    void handleCommand_help_routedCorrectly() {
        CommandResponse response = commandHandler.handleCommand(111L, "/help", "User");
        // Проверяем, что /help вернул справочник команд
        assertTrue(response.text().contains("СПРАВОЧНИК КОМАНД"));
    }
}