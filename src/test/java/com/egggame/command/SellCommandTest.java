package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SellCommandTest {
    private SellCommand sellCommand;
    private Player player;
    private Pet pet;
    private PetService petService;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PlayerStorage playerStorage = new PlayerStorage(tempDir.resolve("players.json").toString());

        petService = new PetService(new SpeciesData(), petStorage, config);
        PlayerService playerService = new PlayerService(playerStorage);

        sellCommand = new SellCommand(petService, playerService);

        player = new Player(111L, "TestUser");
        // Создаем обычного питомца 1-го уровня (базовая цена редкости COMMON = 10)
        pet = petService.createPet(player, "mouse");
    }

    @Test
    void execute_sellPet_successAndGivesCoins() {
        CommandResponse response = sellCommand.execute(player, new String[]{pet.getId()});

        // Проверяем, что начислились монеты (10 за COMMON + 0 за уровень)
        assertEquals(10, player.getCoins());

        // Проверяем, что питомца больше нет в коллекции игрока
        assertTrue(player.getPetIds().isEmpty());

        // Проверяем текст ответа
        assertTrue(response.text().contains("Ты продал"));
    }

    @Test
    void execute_sellPetOnArena_fails() {
        // Отправляем питомца на арену (ставим время окончания в будущее)
        pet.setArenaEndTime(LocalDateTime.now().plusHours(1));
        petService.savePet(pet);

        CommandResponse response = sellCommand.execute(player, new String[]{pet.getId()});

        // Проверяем, что продажа не прошла и монеты не начислились
        assertEquals(0, player.getCoins());
        assertTrue(response.text().contains("на арене"));
    }
}