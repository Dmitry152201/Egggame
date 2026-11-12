package com.egggame.command;

import com.egggame.model.Player;
import com.egggame.service.PlayerService;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class StartCommandTest {
    private StartCommand startCommand;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        PlayerStorage playerStorage = new PlayerStorage(tempDir.resolve("players.json").toString());
        startCommand = new StartCommand(new PlayerService(playerStorage));
    }

    @Test
    void execute_newPlayer_givesFirstEggAndPinsMessage() {
        Player player = new Player(111L, "NewUser");
        CommandResponse response = startCommand.execute(player, new String[]{});

        // Проверяем яйцо
        assertEquals(1, player.getEggs());
        // Проверяем, что в тексте есть "ДОБРО ПОЖАЛОВАТЬ" (из нового туториала)
        assertTrue(response.text().contains("ДОБРО ПОЖАЛОВАТЬ"));
        // Проверяем, что сообщение закрепляется
        assertTrue(response.pin());
    }
}