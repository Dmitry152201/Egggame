package com.egggame.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfig {
    private final Properties props = new Properties();

    public BotConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("bot.properties")) {
            if (input == null) throw new IllegalStateException("Файл bot.properties не найден!");
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка чтения bot.properties", e);
        }
    }

    public String getBotToken() { return props.getProperty("bot.token"); }
    public String getBotUsername() { return props.getProperty("bot.username"); }
    public boolean isProxyEnabled() { return Boolean.parseBoolean(props.getProperty("proxy.enabled", "false")); }
    public String getProxyHost() { return props.getProperty("proxy.host", "127.0.0.1"); }
    public int getProxyPort() { return Integer.parseInt(props.getProperty("proxy.port", "10809")); }
}