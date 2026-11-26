package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Species;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;

public class SellCommand implements Command {
    private final PetService petService;
    private final PlayerService playerService;

    public SellCommand(PetService petService, PlayerService playerService) {
        this.petService = petService;
        this.playerService = playerService;
    }

    @Override
    public String getName() {
        return "/sell";
    }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 1) return new CommandResponse("❌ Укажи номер или имя питомца. Формат: /sell <номер/имя>", null);

        Pet pet = petService.findPet(player, args[0]);
        if (pet == null) return new CommandResponse("❌ Питомец не найден.", null);
        if (pet.isOnArena()) return new CommandResponse("❌ Нельзя продать питомца, пока он на арене!", null);

        Species species = petService.getSpecies(pet.getSpeciesId());

        // Цена: базовая цена редкости + 5 монет за каждый уровень выше первого
        int price = species.getRarity().getBaseSellPrice() + (pet.getLevel() - 1) * 5;

        // Удаляем питомца из хранилища и из списка игрока
        petService.deletePet(pet.getId());
        player.removePetId(pet.getId());

        // Начисляем монеты и сохраняем игрока
        player.addCoins(price);
        playerService.savePlayer(player);

        return new CommandResponse("💸 Ты продал " + pet.getName() + " (" + species.getRarity().getDisplayName() + ") за " + price + " монет.\nТвой баланс: " + player.getCoins() + " монет.", null);
    }
}