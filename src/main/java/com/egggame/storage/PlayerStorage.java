package com.egggame.storage;

import com.egggame.model.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище игроков в формате JSON.
 */
public class PlayerStorage {
    private final String storageFile;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final Map<Long, Player> players = new HashMap<>();

    public PlayerStorage() throws IOException {
        this("storageData/players.json");
    }

    public PlayerStorage(String storageFile) throws IOException {
        this.storageFile = storageFile;
        load();
    }

    public void load() throws IOException {
        Path path = Path.of(storageFile);
        if (!Files.exists(path) || Files.size(path) == 0) return;

        try (Reader reader = Files.newBufferedReader(path)) {
            Type type = new TypeToken<Map<Long, Player>>() {}.getType();
            Map<Long, Player> loaded = gson.fromJson(reader, type);
            if (loaded != null) players.putAll(loaded);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("⚠️ Ошибка загрузки игроков: " + e.getMessage());
        }
    }

    public void save() {
        try {
            Path path = Path.of(storageFile);
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(players, writer);
            }
        } catch (IOException e) {
            System.err.println("❌ Ошибка сохранения игроков: " + e.getMessage());
        }
    }

    public Player getPlayer(long telegramId) { return players.get(telegramId); }
    public void savePlayer(Player player) { players.put(player.getTelegramId(), player); save(); }
    public boolean hasPlayer(long telegramId) { return players.containsKey(telegramId); }
}