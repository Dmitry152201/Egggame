package com.egggame;

import com.egggame.command.CommandHandler;
import com.egggame.config.Config;
import com.egggame.data.SpeciesData;
import com.egggame.service.EggService;
import com.egggame.service.IncubatorService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

/**
 * Точка входа в приложение EGGGAME.
 * Инициализирует все компоненты и запускает бота.
 */
public class Main {

    /**
     * Запускает бота EGGGAME.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            Config config = new Config();

            DefaultBotOptions botOptions = new DefaultBotOptions();
            if (config.isProxyEnabled()) {
                botOptions.setProxyHost(config.getProxyHost());
                botOptions.setProxyPort(config.getProxyPort());
                botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            }

            SpeciesData speciesData = new SpeciesData();
            PlayerStorage playerStorage = new PlayerStorage();
            PetStorage petStorage = new PetStorage();

            PlayerService playerService = new PlayerService(playerStorage);
            EggService eggService = new EggService(config);
            PetService petService = new PetService(speciesData, petStorage);
            IncubatorService incubatorService = new IncubatorService(config, petService);

            CommandHandler commandHandler = new CommandHandler(
                    playerService, eggService, incubatorService, petService, speciesData
            );

            Bot bot = new Bot(botOptions, config, commandHandler);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);

            System.out.println("✅ Бот EGGGAME успешно запущен!");
        } catch (TelegramApiException | IOException e) {
            System.err.println("❌ Ошибка запуска бота: " + e.getMessage());
            e.printStackTrace();
        }
    }
}