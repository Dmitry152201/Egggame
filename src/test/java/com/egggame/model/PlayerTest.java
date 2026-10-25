package com.egggame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты модели игрока.
 */
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(123456789L, "TestPlayer");
    }

    @Test
    void player_initialState_correct() {
        assertEquals(123456789L, player.getTelegramId());
        assertEquals("TestPlayer", player.getUsername());
        assertEquals(0, player.getEggs());
        assertTrue(player.isIncubatorEmpty());
        assertNull(player.getLastDailyAt());
    }

    @Test
    void player_addEgg_increasesCount() {
        player.addEgg();
        assertEquals(1, player.getEggs());
        player.addEgg();
        assertEquals(2, player.getEggs());
    }

    @Test
    void player_spendEgg_decreasesCount() {
        player.setEggs(3);
        player.spendEgg();
        assertEquals(2, player.getEggs());
    }

    @Test
    void player_isEggReady_returnsTrueWhenTimeComes() {
        player.setHatchReadyAt(LocalDateTime.now().minusSeconds(10));
        assertTrue(player.isEggReady(LocalDateTime.now()));
    }

    @Test
    void player_isEggReady_returnsFalseWhenNotReady() {
        player.setHatchReadyAt(LocalDateTime.now().plusSeconds(100));
        assertFalse(player.isEggReady(LocalDateTime.now()));
    }

    @Test
    void player_hasSpecies_returnsTrueIfOwned() {
        player.addPetId("mouse_1");
        assertTrue(player.hasSpecies("mouse"));
        assertFalse(player.hasSpecies("dog_bird"));
    }
}