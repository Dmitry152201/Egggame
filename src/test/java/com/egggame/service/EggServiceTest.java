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

    @Test
    void canGetDaily_rightAfterGrant_returnsFalse() {
        // Выдаем яйцо, время последнего получения записывается на текущий момент
        eggService.grantDailyEgg(player);

        // Сразу же пытаемся получить еще раз
        assertFalse(eggService.canGetDaily(player));
    }

    @Test
    void canGetDaily_afterCooldown_returnsTrue() {
        eggService.grantDailyEgg(player);

        // Принудительно ставим время получения в прошлое (больше, чем кулдаун)
        player.setLastDailyAt(java.time.LocalDateTime.now().minusSeconds(10));

        assertTrue(eggService.canGetDaily(player));
    }
}