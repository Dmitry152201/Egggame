package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Player;
import com.egggame.service.CombatService;
import com.egggame.service.PetService;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FightCommandTest {
    private FightCommand fightCommand;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        PetService petService = new PetService(new SpeciesData(), petStorage, config);
        CombatService combatService = new CombatService(petService);
        fightCommand = new FightCommand(petService, combatService);

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
}