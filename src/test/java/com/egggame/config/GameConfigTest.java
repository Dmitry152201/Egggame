package com.egggame.config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class GameConfigTest {
    @Test
    void gameConfig_debugMode_returnsCorrectValues() {
        assertTrue(new GameConfig().isDebugEnabled());
    }
}