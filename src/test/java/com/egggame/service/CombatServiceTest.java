package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.storage.PetStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class CombatServiceTest {
    private CombatService combatService;
    private PetService petService;
    private Player player;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        GameConfig config = new GameConfig();
        PetStorage petStorage = new PetStorage(tempDir.resolve("pets.json").toString());
        petService = new PetService(new SpeciesData(), petStorage, config);
        combatService = new CombatService(petService);
        player = new Player(111L, "TestPlayer");
    }

    @Test
    void fight_winAgainstWeakEnemy_returnsVictoryAndXp() {
        Pet pet = petService.createPet(player, "shadow_gentleman");
        CombatService.CombatResult result = combatService.fight(pet, 1);
        assertTrue(result.victory());
        assertEquals(5, result.xpGained());
    }

    @Test
    void fight_loseAgainstStrongEnemy_returnsDefeat() {
        Pet pet = petService.createPet(player, "mouse");
        pet.setCurrentHp(1);
        CombatService.CombatResult result = combatService.fight(pet, 50);
        assertFalse(result.victory());
    }

    @Test
    void fight_hungryAndSadPet_refusesToFight() {
        Pet pet = petService.createPet(player, "mouse");
        pet.setSatiety(0);
        pet.setHappiness(0);
        CombatService.CombatResult result = combatService.fight(pet, 1);
        assertFalse(result.victory());
        assertTrue(result.log().contains("отказывается сражаться"));
    }
}