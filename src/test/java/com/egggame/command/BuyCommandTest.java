package com.egggame.command;

import com.egggame.model.Player;
import com.egggame.service.PlayerService;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuyCommandTest {
    private BuyCommand buyCommand;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        PlayerStorage playerStorage = new PlayerStorage(tempDir.resolve("players.json").toString());
        PlayerService playerService = new PlayerService(playerStorage);
        buyCommand = new BuyCommand(playerService);

        player = new Player(111L, "TestUser");
    }

    @Test
    void execute_buyEgg_success() {
        player.addCoins(25); // Даем 25 монет (яйцо стоит 20)

        CommandResponse response = buyCommand.execute(player, new String[]{"egg"});

        // Проверяем, что монеты списались и яйцо добавилось
        assertEquals(5, player.getCoins());
        assertEquals(1, player.getEggs());
        assertTrue(response.text().contains("купил яйцо"));
    }

    @Test
    void execute_notEnoughCoins_fails() {
        player.addCoins(10); // Даем 10 монет (не хватает)

        CommandResponse response = buyCommand.execute(player, new String[]{"egg"});

        // Проверяем, что ничего не изменилось
        assertEquals(10, player.getCoins());
        assertEquals(0, player.getEggs());
        assertTrue(response.text().contains("недостаточно монет"));
    }

    @Test
    void execute_invalidItem_fails() {
        CommandResponse response = buyCommand.execute(player, new String[]{"bread"});
        assertTrue(response.text().contains("Что купить?"));
    }
}