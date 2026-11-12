package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;

public class RenameCommand implements Command {
    private final PetService petService;

    public RenameCommand(PetService petService) {
        this.petService = petService;
    }

    @Override public String getName() { return "/rename"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 2) {
            return new CommandResponse("❌ Формат: /rename <номер_или_имя> <новое_имя>", null);
        }

        Pet pet = petService.findPet(player, args[0]);
        if (pet == null) return new CommandResponse("❌ Питомец не найден.", null);

        // Проверка на арену
        if (pet.isOnArena()) {
            return new CommandResponse("❌ " + pet.getName() + " на арене, его нельзя переименовать прямо сейчас.", null);
        }

        StringBuilder newNameBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) newNameBuilder.append(" ");
            newNameBuilder.append(args[i]);
        }
        String newName = newNameBuilder.toString();

        pet.setName(newName);
        petService.savePet(pet);

        return new CommandResponse("✅ Успешно! Теперь твоего питомца зовут " + newName + ".", null, KeyboardFactory.petActions(pet.getId(), true));
    }
}