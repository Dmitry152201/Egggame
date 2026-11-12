package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты сервиса выдачи яиц.
 */
class EggServiceTest {

    private EggService eggService;
    private Player player;

    @BeforeEach
    void setUp() {
        GameConfig config = new GameConfig();
        eggService = new EggService(config);
        player = new Player(123456789L, "TestPlayer");
    }

    @Test
    void canGetDaily_newPlayer_returnsTrue() {
        assertTrue(eggService.canGetDaily(player));
    }

    @Test
    void grantDailyEgg_increasesEggs() {
        eggService.grantDailyEgg(player);
        assertEquals(1, player.getEggs());
    }

    @Test
    void formatTimeRemaining_seconds_returnsSeconds() {
        assertEquals("30 сек", eggService.formatTimeRemaining(30));
    }
}