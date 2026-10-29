package com.egggame.service;

import com.egggame.Config;
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
        Config config = new Config();
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

    @Test
    void formatTimeRemaining_minutes_returnsMinutes() {
        assertEquals("5 мин", eggService.formatTimeRemaining(300));
    }

    @Test
    void formatTimeRemaining_hours_returnsHoursAndMinutes() {
        assertEquals("2 ч 30 мин", eggService.formatTimeRemaining(9000));
    }
}