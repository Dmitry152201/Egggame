package com.egggame.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты конфигурации бота.
 */
class ConfigTest {

    /**
     * Проверяет, что токен и имя загружены (не null).
     */
    @Test
    void testConfig_loadsSuccessfully() {
        Config config = new Config();
        assertNotNull(config.getBotToken());
        assertNotNull(config.getBotUsername());
    }

    /**
     * Проверяет правильность математики таймеров.
     */
    @Test
    void testConfig_debugMode_returnsCorrectValues() {
        Config config = new Config();
        if (config.isDebugEnabled()) {
            assertEquals(5, config.getIncubatorMinSeconds());
            assertEquals(20, config.getDailyCooldownSeconds());
        } else {
            assertEquals(21600, config.getIncubatorMinSeconds()); // 6 часов
            assertEquals(86400, config.getDailyCooldownSeconds()); // 24 часа
        }
    }
}