package com.egggame.storage;

import com.egggame.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStorageTest {
    @Test
    void savePlayer_andGetPlayer_returnsPlayer(@TempDir Path tempDir) throws IOException {
        PlayerStorage storage = new PlayerStorage(tempDir.resolve("players.json").toString());
        Player player = new Player(123456789L, "TestPlayer");
        player.addEgg();
        storage.savePlayer(player);

        Player loaded = storage.getPlayer(123456789L);
        assertNotNull(loaded);
        assertEquals(1, loaded.getEggs());
    }

    @Test
    void hasPlayer_worksCorrectly(@TempDir Path tempDir) throws IOException {
        PlayerStorage storage = new PlayerStorage(tempDir.resolve("players.json").toString());
        Player player = new Player(987654321L, "TestPlayer2");
        storage.savePlayer(player);

        assertTrue(storage.hasPlayer(987654321L));
        assertFalse(storage.hasPlayer(111111111L));
    }
}