package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;

public class FeedCommand implements Command {
    private final PetService petService;
    private final PlayerService playerService;

    public FeedCommand(PetService petService, PlayerService playerService) {
        this.petService = petService;
        this.playerService = playerService;
    }

    @Override public String getName() { return "/feed"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 1) return new CommandResponse("❌ Укажи номер или имя.", null);
        Pet pet = petService.findPet(player, args[0]);
        if (pet == null) return new CommandResponse("❌ Не найден.", null);

        // Проверка на арену
        if (pet.isOnArena()) {
            return new CommandResponse("❌ " + pet.getName() + " сейчас на арене и не может есть! Дождись его возвращения.", null);
        }

        if (player.getEggs() <= 0) return new CommandResponse("❌ Нет яиц. Используй /daily.", null);

        petService.updatePetStats(pet);
        int oldSatiety = pet.getSatiety();
        int oldHp = pet.getCurrentHp();

        pet.setSatiety(oldSatiety + 25);
        pet.setCurrentHp(oldHp + 20);

        player.spendEgg();
        playerService.savePlayer(player);
        petService.savePet(pet);

        String text = "🍖 Покормил. Сытость: " + oldSatiety + " → " + pet.getSatiety() + ".\n";
        if (pet.getCurrentHp() > oldHp) text += "❤️ Здоровье: " + oldHp + " → " + pet.getCurrentHp() + ".\n";
        text += "Яиц осталось: " + player.getEggs();

        return new CommandResponse(text, null, KeyboardFactory.petActions(pet.getId(), true));
    }
}