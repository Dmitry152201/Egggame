package com.egggame.service;

import com.egggame.config.Config;
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

    /**
     * Проверяет, что яйцо начисляется и время фиксируется.
     */
    @Test
    void testGrantDailyEgg_increasesEggsAndSetsTime() {
        eggService.grantDailyEgg(player);
        assertEquals(1, player.getEggs());
        assertNotNull(player.getLastDailyAt());
    }

    /**
     * Проверяет противоположный случай: после получения /daily игрок не может получить его снова.
     */
    @Test
    void testCanGetDaily_afterGrant_returnsFalse() {
        eggService.grantDailyEgg(player);
        assertFalse(eggService.canGetDaily(player));
    }
}