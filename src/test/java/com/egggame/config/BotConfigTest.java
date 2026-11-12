package com.egggame.config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class BotConfigTest {
    @Test
    void botConfig_loadsSuccessfully() {
        assertEquals("TEST_TOKEN_FOR_JUNIT", new BotConfig().getBotToken());
    }
}