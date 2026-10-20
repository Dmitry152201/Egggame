package com.egggame;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Настройки бота EGGGAME, загружаемые из bot.properties.
 * Файл не коммитится в git, поэтому токен не попадает в репозиторий.
 */
public class Config {

    /** Имя файла с реальными секретами, в git не коммитится */
    private static final String CONFIG_FILE = "bot.properties";

    /** Имя файла-образца, который лежит в git */
    private static final String EXAMPLE_FILE = "bot.properties.example";

    /** Загруженные свойства конфигурации */
    private final Properties props = new Properties();

    /**
     * Загружает настройки из bot.properties.
     *
     * @throws IllegalStateException если файл отсутствует или не читается
     */
    public Config() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Файл " + CONFIG_FILE + " не найден в src/main/resources! "
                                + "Скопируй " + EXAMPLE_FILE + " в " + CONFIG_FILE
                                + " и вставь свой токен.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать " + CONFIG_FILE, e);
        }
    }

    /**
     * Возвращает токен бота.
     *
     * @return токен Telegram Bot API из конфига
     */
    public String getBotToken() {
        return props.getProperty("bot.token");
    }

    /**
     * Возвращает username бота.
     *
     * @return username бота без собачки
     */
    public String getBotUsername() {
        return props.getProperty("bot.username");
    }

    /**
     * Проверяет, включён ли прокси.
     *
     * @return true, если бот должен ходить в Telegram через прокси
     */
    public boolean isProxyEnabled() {
        return Boolean.parseBoolean(props.getProperty("proxy.enabled", "false"));
    }

    /**
     * Возвращает хост прокси.
     *
     * @return адрес прокси-сервера
     */
    public String getProxyHost() {
        return props.getProperty("proxy.host", "127.0.0.1");
    }

    /**
     * Возвращает порт прокси.
     *
     * @return порт прокси-сервера
     */
    public int getProxyPort() {
        return Integer.parseInt(props.getProperty("proxy.port", "10809"));
    }

    /**
     * Проверяет, включён ли debug-режим для демонстрации.
     *
     * @return true, если таймеры ускорены (вылупление за секунды)
     */
    public boolean isDebugEnabled() {
        return Boolean.parseBoolean(props.getProperty("debug.enabled", "false"));
    }

    /**
     * Возвращает кулдаун /daily в секундах.
     * В debug-режиме укорочен до нескольких секунд для демонстрации.
     *
     * @return кулдаун в секундах (24 часа = 86400 в обычном режиме)
     */
    public long getDailyCooldownSeconds() {
        if (isDebugEnabled()) {
            return Long.parseLong(props.getProperty("debug.daily.cooldown.seconds", "20"));
        }
        return Long.parseLong(props.getProperty("daily.cooldown.hours", "24")) * 3600L;
    }

    /**
     * Возвращает минимальное время инкубации в секундах.
     * В debug-режиме равно секундам ускоренного вылупления.
     *
     * @return минимум инкубации в секундах (6 часов в обычном режиме)
     */
    public long getIncubatorMinSeconds() {
        if (isDebugEnabled()) {
            return Long.parseLong(props.getProperty("debug.hatch.seconds", "5"));
        }
        return Long.parseLong(props.getProperty("incubator.min.hours", "6")) * 3600L;
    }

    /**
     * Возвращает максимальное время инкубации в секундах.
     * В debug-режиме равно секундам ускоренного вылупления.
     *
     * @return максимум инкубации в секундах (12 часов в обычном режиме)
     */
    public long getIncubatorMaxSeconds() {
        if (isDebugEnabled()) {
            return Long.parseLong(props.getProperty("debug.hatch.seconds", "5"));
        }
        return Long.parseLong(props.getProperty("incubator.max.hours", "12")) * 3600L;
    }
}