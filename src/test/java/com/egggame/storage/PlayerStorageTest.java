package com.egggame.storage;

import com.egggame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты хранилища игроков.
 */
class PlayerStorageTest {

    private PlayerStorage playerStorage;

    @BeforeEach
    void setUp() throws IOException {
        playerStorage = new PlayerStorage();
    }

    @Test
    void savePlayer_andGetPlayer_returnsPlayer() {
        Player player = new Player(123456789L, "TestPlayer");
        player.addEgg();
        playerStorage.savePlayer(player);

        Player loaded = playerStorage.getPlayer(123456789L);
        assertNotNull(loaded);
        assertEquals(1, loaded.getEggs());
    }

    @Test
    void hasPlayer_existingPlayer_returnsTrue() {
        Player player = new Player(987654321L, "TestPlayer2");
        playerStorage.savePlayer(player);

        assertTrue(playerStorage.hasPlayer(987654321L));
    }

    @Test
    void hasPlayer_nonExistingPlayer_returnsFalse() {
        assertFalse(playerStorage.hasPlayer(111111111L));
    }
}