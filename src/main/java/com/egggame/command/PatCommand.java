package com.egggame.command;

import com.egggame.config.GameConfig;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import java.time.Duration;
import java.time.LocalDateTime;

public class PatCommand implements Command {
    private final PetService petService;
    private final GameConfig config;

    public PatCommand(PetService petService, GameConfig config) {
        this.petService = petService;
        this.config = config;
    }

    @Override public String getName() { return "/pat"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 1) return new CommandResponse("❌ Укажи номер или имя.", null);
        Pet pet = petService.findPet(player, args[0]);
        if (pet == null) return new CommandResponse("❌ Не найден.", null);

        // Проверка на арену
        if (pet.isOnArena()) {
            return new CommandResponse("❌ " + pet.getName() + " сейчас на арене и не может принимать ласку. Дождись его возвращения!", null);
        }

        petService.updatePetStats(pet);
        long cd = config.getPatCooldownSeconds();

        if (pet.getLastPatAt() != null) {
            long passed = Duration.between(pet.getLastPatAt(), LocalDateTime.now()).getSeconds();
            if (passed < cd) {
                long remaining = cd - passed;
                String timeLeft = remaining > 60
                        ? (remaining / 60) + " мин " + (remaining % 60) + " сек"
                        : remaining + " сек";
                return new CommandResponse("⏳ " + pet.getName() + " еще не готов к ласке. Подожди " + timeLeft + ".", null, KeyboardFactory.petActions(pet.getId(), true));
            }
        }

        int old = pet.getHappiness();
        pet.setHappiness(old + 5);
        pet.setLastPatAt(LocalDateTime.now());
        petService.savePet(pet);

        return new CommandResponse("💖 Ты погладил " + pet.getName() + ". Счастье: " + old + " → " + pet.getHappiness(), null, KeyboardFactory.petActions(pet.getId(), true));
    }
}