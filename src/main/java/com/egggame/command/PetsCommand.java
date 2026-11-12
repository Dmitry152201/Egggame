package com.egggame.command;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.PetService;
import java.util.List;
public class PetsCommand implements Command {
    private final PetService petService;
    public PetsCommand(PetService petService) { this.petService = petService; }
    @Override public String getName() { return "/pets"; }
    @Override public CommandResponse execute(Player player, String[] args) {
        List<Pet> pets = petService.getPlayerPets(player);
        if (pets.isEmpty()) return new CommandResponse("🐾 Коллекция пуста. Используй /incubate.", null, null);

        StringBuilder sb = new StringBuilder("🐾 Твои питомцы (нажми номер, чтобы открыть):\n\n");
        for (int i = 0; i < pets.size(); i++) {
            sb.append(i + 1).append(". ").append(pets.get(i).getName()).append("\n");
        }
        return new CommandResponse(sb.toString(), null, KeyboardFactory.petList(pets));
    }
}