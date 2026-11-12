package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import java.util.List;

/** Команда /profile. Показывает статистику игрока. */
public class ProfileCommand implements Command {
    private final PetService petService;

    public ProfileCommand(PetService petService) {
        this.petService = petService;
    }

    @Override
    public String getName() { return "/profile"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        List<Pet> pets = petService.getPlayerPets(player);
        int avgHappy = 0;
        if (!pets.isEmpty()) {
            int total = 0;
            for (Pet p : pets) {
                petService.updatePetStats(p);
                total += p.getHappiness();
            }
            avgHappy = total / pets.size();
        }
        return new CommandResponse("👤 Профиль: " + player.getUsername() + "\n🥚 Яиц: " + player.getEggs() + "\n🐾 Питомцев: " + pets.size() + "\n😊 Среднее счастье: " + avgHappy + "/100", null);
    }
}