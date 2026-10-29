package com.egggame.config;

import com.egggame.Config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты конфигурации бота.
 */
class ConfigTest {

    @Test
    void config_loadsSuccessfully() {
        Config config = new Config();
        assertNotNull(config.getBotToken());
        assertNotNull(config.getBotUsername());
    }

    @Test
    void config_debugMode_returnsCorrectValues() {
        Config config = new Config();
        // Если debug.enabled=true в bot.properties
        if (config.isDebugEnabled()) {
            assertEquals(5, config.getIncubatorMinSeconds());
            assertEquals(5, config.getIncubatorMaxSeconds());
            assertEquals(20, config.getDailyCooldownSeconds());
        } else {
            assertEquals(21600, config.getIncubatorMinSeconds()); // 6 часов
            assertEquals(43200, config.getIncubatorMaxSeconds()); // 12 часов
            assertEquals(86400, config.getDailyCooldownSeconds()); // 24 часа
        }
    }

    @Test
    void config_proxySettings_correct() {
        Config config = new Config();
        assertTrue(config.isProxyEnabled());
        assertEquals("127.0.0.1", config.getProxyHost());
        assertEquals(10809, config.getProxyPort());
    }
}