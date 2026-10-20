package com.egggame.service;

import com.egggame.model.Player;
import com.egggame.storage.PlayerStorage;

/**
 * Сервис управления игроками.
 * Отвечает за создание и сохранение игроков.
 */
public class PlayerService {

    /** Хранилище игроков */
    private final PlayerStorage playerStorage;

    /**
     * Создаёт сервис управления игроками.
     *
     * @param playerStorage хранилище игроков
     */
    public PlayerService(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
    }

    /**
     * Возвращает существующего игрока или создаёт нового.
     *
     * @param telegramId Telegram ID игрока
     * @param username   Telegram-имя игрока
     * @return игрок (существующий или новосозданный)
     */
    public Player getOrCreate(long telegramId, String username) {
        Player player = playerStorage.getPlayer(telegramId);
        if (player == null) {
            player = new Player(telegramId, username);
            playerStorage.savePlayer(player);
        }
        return player;
    }

    /**
     * Сохраняет изменения игрока в хранилище.
     *
     * @param player игрок для сохранения
     */
    public void savePlayer(Player player) {
        playerStorage.savePlayer(player);
    }
}