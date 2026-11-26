package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.CombatService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;
import com.egggame.storage.PetStorage;
import com.egggame.storage.PlayerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FightCommandTest {
    private FightCommand fightCommand;
    private Player player;
    private PlayerService playerService;
    private PetService petService;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PlayerStorage playerStorage = new PlayerStorage(tempDir.resolve("players.json").toString());

        petService = new PetService(new SpeciesData(), petStorage, config);
        CombatService combatService = new CombatService(petService);
        playerService = new PlayerService(playerStorage);

        // ОБНОВЛЕНО: добавлен playerService в конструктор
        fightCommand = new FightCommand(petService, combatService, playerService);

        player = new Player(111L, "TestUser");
        petService.createPet(player, "mouse");
    }

    @Test
    void execute_validArgs_returnsCombatLog() {
        CommandResponse response = fightCommand.execute(player, new String[]{"1", "2"});
        assertTrue(response.text().contains("вступает в бой"));
    }

    @Test
    void execute_invalidArgs_returnsError() {
        CommandResponse response = fightCommand.execute(player, new String[]{"1"});
        assertTrue(response.text().contains("Формат"));
    }

    @Test
    void execute_win_addsCoinsToPlayer() {
        // Делаем питомца очень сильным, чтобы гарантировать победу
        Pet pet = petService.getPlayerPets(player).get(0);
        pet.setLevel(50); // Завышаем уровень для 100% победы
        pet.recalculateStats(petService.getSpecies(pet.getSpeciesId()));
        petService.savePet(pet);

        fightCommand.execute(player, new String[]{"1", "1"}); // Бьемся с врагом 1 уровня

        // Проверяем, что монеты начислились (за 1 уровень врага дают 2 монеты)
        assertEquals(2, player.getCoins());
    }
}