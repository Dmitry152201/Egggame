package com.egggame.command;

import com.egggame.config.Config;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.EggService;
import com.egggame.service.IncubatorService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты обработчика команд.
 */
class CommandHandlerTest {

    private CommandHandler commandHandler;
    private PlayerService playerService;
    private PetService petService;
    private long testPlayerId; // Уникальный ID для каждого теста

    @BeforeEach
    void setUp() throws IOException {
        Config config = new Config();
        SpeciesData speciesData = new SpeciesData();
        PlayerStorage playerStorage = new PlayerStorage();
        PetStorage petStorage = new PetStorage();

        playerService = new PlayerService(playerStorage);
        EggService eggService = new EggService(config);
        petService = new PetService(speciesData, petStorage);
        IncubatorService incubatorService = new IncubatorService(config, petService);

        commandHandler = new CommandHandler(playerService, eggService, incubatorService, petService, speciesData, config);

        // Генерируем уникальный ID, чтобы не пересекаться с сохранениями в JSON
        testPlayerId = System.currentTimeMillis();
    }

    @Test
    void testStartCommand_grantsFirstEggAndGreets() {
        CommandResponse response = commandHandler.handleCommand(testPlayerId, "/start", "TestUser");

        assertEquals("🐣 Привет! Это бот EGGGAME.\n\nТебе выдано первое яйцо! Используй /incubate, чтобы положить его в инкубатор. Или /help для списка команд.", response.getText());

        Player player = playerService.getOrCreate(testPlayerId, "TestUser");
        assertEquals(1, player.getEggs());
    }

    @Test
    void testFeedCommand_success_increasesSatietyAndSpendsEgg() {
        Player player = playerService.getOrCreate(testPlayerId, "TestUser");
        player.addEgg();
        Pet pet = petService.createPet(player, "mouse");

        CommandResponse response = commandHandler.handleCommand(testPlayerId, "/feed 1", "TestUser");

        assertTrue(response.getText().contains("Сытость: 50 → 75"));
        assertEquals(0, player.getEggs());
        assertEquals(75, pet.getSatiety());
    }

    @Test
    void testFeedCommand_noEggs_returnsError() {
        Player player = playerService.getOrCreate(testPlayerId, "TestUser");
        petService.createPet(player, "mouse");

        CommandResponse response = commandHandler.handleCommand(testPlayerId, "/feed 1", "TestUser");

        assertEquals("❌ У тебя нет яиц. Используй /daily.", response.getText());
    }

    @Test
    void testPatCommand_success_increasesHappiness() {
        Player player = playerService.getOrCreate(testPlayerId, "TestUser");
        Pet pet = petService.createPet(player, "mouse");

        CommandResponse response = commandHandler.handleCommand(testPlayerId, "/pat Сырный Мышонок", "TestUser");

        assertTrue(response.getText().contains("Счастье: 50 → 55"));
        assertEquals(55, pet.getHappiness());
    }

    @Test
    void testPatCommand_cooldown_returnsError() {
        Player player = playerService.getOrCreate(testPlayerId, "TestUser");
        Pet pet = petService.createPet(player, "mouse");
        pet.setLastPatAt(LocalDateTime.now());

        CommandResponse response = commandHandler.handleCommand(testPlayerId, "/pat 1", "TestUser");

        assertTrue(response.getText().startsWith("⏳ Питомец еще не готов к ласке."));
    }
}