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
 * Хранилище игроков с сохранением в JSON-файл.
 * Использует кастомный адаптер для LocalDateTime и защищено от чтения пустых файлов.
 */
public class PlayerStorage {

    /** Путь к файлу хранилища */
    private static final String STORAGE_FILE = "storage/players.json";

    /** Gson с кастомным адаптером для LocalDateTime */
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    /** Кэш игроков в памяти */
    private final Map<Long, Player> players = new HashMap<>();

    /**
     * Создаёт хранилище и загружает данные из файла.
     */
    public PlayerStorage() throws IOException {
        load();
    }

    /**
     * Загружает игроков из JSON-файла.
     */
    public void load() throws IOException {
        Path path = Path.of(STORAGE_FILE);

        // ЗАЩИТА: если файла нет или он пустой (0 байт), просто выходим
        if (!Files.exists(path) || Files.size(path) == 0) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            Type type = new TypeToken<Map<Long, Player>>() {}.getType();
            Map<Long, Player> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                players.putAll(loaded);
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("⚠️ Ошибка загрузки игроков (файл повреждён или пуст, начинаем с чистого листа): " + e.getMessage());
            // Бот не падает, а просто продолжает работу с пустой картой
        }
    }

    /**
     * Сохраняет всех игроков в JSON-файл.
     */
    public void save() {
        try {
            Files.createDirectories(Path.of("storage"));
            try (Writer writer = Files.newBufferedWriter(Path.of(STORAGE_FILE))) {
                gson.toJson(players, writer);
            }
        } catch (IOException e) {
            System.err.println("❌ Ошибка сохранения игроков: " + e.getMessage());
        }
    }

    /**
     * Возвращает игрока по Telegram ID.
     *
     * @param telegramId Telegram ID игрока
     * @return игрок или null, если не найден
     */
    public Player getPlayer(long telegramId) {
        return players.get(telegramId);
    }

    /**
     * Сохраняет игрока в хранилище и в файл.
     *
     * @param player игрок для сохранения
     */
    public void savePlayer(Player player) {
        players.put(player.getTelegramId(), player);
        save();
    }

    /**
     * Проверяет, существует ли игрок с данным Telegram ID.
     *
     * @param telegramId Telegram ID для проверки
     * @return true, если игрок найден
     */
    public boolean hasPlayer(long telegramId) {
        return players.containsKey(telegramId);
    }
}