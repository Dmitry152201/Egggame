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

    /**
     * Проверяет начальное состояние (0 яиц, пустой инкубатор).
     */
    @Test
    void testPlayer_initialState_correct() {
        assertEquals(0, player.getEggs());
        assertTrue(player.isIncubatorEmpty());
        assertNull(player.getLastDailyAt());
    }

    /**
     * Проверяет корректное списание яиц.
     */
    @Test
    void testSpendEgg_decreasesCount() {
        player.setEggs(3);
        player.spendEgg();
        assertEquals(2, player.getEggs());
    }
}