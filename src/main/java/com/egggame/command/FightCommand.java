package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.CombatService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;

public class FightCommand implements Command {
    private final PetService petService;
    private final CombatService combatService;
    private final PlayerService playerService;

    public FightCommand(PetService petService, CombatService combatService, PlayerService playerService) {
        this.petService = petService;
        this.combatService = combatService;
        this.playerService = playerService;
    }

    @Override
    public String getName() {
        return "/fight";
    }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 2) return new CommandResponse("❌ Формат: /fight <имя/номер> <уровень_врага>", null);

        Pet pet = petService.findPet(player, args[0]);
        if (pet == null) return new CommandResponse("❌ Питомец не найден.", null);
        if (pet.isOnArena()) return new CommandResponse("❌ Питомец на арене и не может драться!", null);

        int enemyLevel;
        try {
            enemyLevel = Integer.parseInt(args[1]);
            if (enemyLevel <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return new CommandResponse("❌ Уровень врага должен быть положительным числом.", null);
        }

        petService.updatePetStats(pet);

        CombatService.CombatResult result = combatService.fight(pet, enemyLevel);

        if (result.victory()) {
            player.addCoins(result.coinsGained());
            playerService.savePlayer(player);
            String finalLog = result.log() + "\n💰 Получено монет: " + result.coinsGained() + "\nТвой баланс: " + player.getCoins();
            return new CommandResponse(finalLog, null, KeyboardFactory.combatActions(pet.getId()));
        }

        return new CommandResponse(result.log(), null, KeyboardFactory.combatActions(pet.getId()));
    }
}