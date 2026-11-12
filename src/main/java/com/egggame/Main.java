package com.egggame;

import com.egggame.command.*;
import com.egggame.config.BotConfig;
import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.service.*;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.List;

/**
 * Главный класс приложения.
 */
public class Main {
    public static void main(String[] args) {
        try {
            BotConfig botConfig = new BotConfig();
            GameConfig gameConfig = new GameConfig();

            DefaultBotOptions botOptions = new DefaultBotOptions();
            if (botConfig.isProxyEnabled()) {
                botOptions.setProxyHost(botConfig.getProxyHost());
                botOptions.setProxyPort(botConfig.getProxyPort());
                botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            }

            SpeciesData speciesData = new SpeciesData();
            PlayerStorage playerStorage = new PlayerStorage();
            PetStorage petStorage = new PetStorage();

            PlayerService playerService = new PlayerService(playerStorage);
            EggService eggService = new EggService(gameConfig);
            PetService petService = new PetService(speciesData, petStorage, gameConfig);
            IncubatorService incubatorService = new IncubatorService(gameConfig, petService);

            CombatService combatService = new CombatService(petService);
            ArenaService arenaService = new ArenaService(petService, gameConfig);

            PetBackgroundWorker backgroundWorker = new PetBackgroundWorker(petService);
            backgroundWorker.start();

            List<Command> commands = List.of(
                    new StartCommand(playerService), new HelpCommand(), new ProfileCommand(petService),
                    new DailyCommand(eggService, playerService), new IncubatorCommand(),
                    new IncubateCommand(incubatorService, playerService), new HatchCommand(incubatorService, playerService),
                    new PetsCommand(petService), new PetCommand(petService), new FeedCommand(petService, playerService),
                    new PatCommand(petService, gameConfig), new RenameCommand(petService),
                    new FightCommand(petService, combatService), new ArenaCommand(petService, arenaService)
            );

            CommandHandler commandHandler = new CommandHandler(playerService, commands);
            Bot bot = new Bot(botOptions, botConfig, commandHandler);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);

            // Убрали "(Задача 3)"
            System.out.println("✅ Бот EGGGAME успешно запущен!");
        } catch (TelegramApiException | IOException e) {
            System.err.println("❌ Ошибка запуска: " + e.getMessage());
            e.printStackTrace();
        }
    }
}