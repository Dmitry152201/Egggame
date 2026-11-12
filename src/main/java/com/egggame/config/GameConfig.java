package com.egggame.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Игровая конфигурация.
 */
public class GameConfig {
    private final Properties props = new Properties();

    public GameConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("bot.properties")) {
            if (input == null) throw new IllegalStateException("Файл bot.properties не найден!");
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка чтения bot.properties", e);
        }
    }

    public boolean isDebugEnabled() { return Boolean.parseBoolean(props.getProperty("debug.enabled", "false")); }

    public long getDailyCooldownSeconds() {
        return isDebugEnabled() ? Long.parseLong(props.getProperty("debug.daily.cooldown.seconds", "20"))
                : Long.parseLong(props.getProperty("daily.cooldown.hours", "24")) * 3600L;
    }

    public long getIncubatorMinSeconds() {
        return isDebugEnabled() ? Long.parseLong(props.getProperty("debug.hatch.seconds", "5"))
                : Long.parseLong(props.getProperty("incubator.min.hours", "6")) * 3600L;
    }

    public long getIncubatorMaxSeconds() {
        return isDebugEnabled() ? Long.parseLong(props.getProperty("debug.hatch.seconds", "5"))
                : Long.parseLong(props.getProperty("incubator.max.hours", "12")) * 3600L;
    }

    public long getPatCooldownSeconds() {
        return isDebugEnabled() ? 10 : 2 * 3600;
    }

    /**
     * Сколько РЕАЛЬНЫХ секунд длится 1 час на арене.
     * В дебаге берем значение из конфига (по умолчанию 10 сек), в обычном режиме 3600 сек (1 час).
     */
    public long getArenaTimeMultiplierSeconds() {
        if (isDebugEnabled()) {
            return Long.parseLong(props.getProperty("debug.arena.hour.seconds", "10"));
        }
        return 3600; // 1 час = 60 минут * 60 секунд
    }
}