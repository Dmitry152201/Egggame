package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Species;
import com.egggame.service.PetService;

public class PetCommand implements Command {
    private final PetService petService;

    public PetCommand(PetService petService) {
        this.petService = petService;
    }

    @Override public String getName() { return "/pet"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 1) return new CommandResponse("❌ Укажи номер или имя.", null, null);

        Pet pet = petService.findPet(player, args[0]);
        if (pet == null) return new CommandResponse("❌ Не найден.", null, null);

        petService.updatePetStats(pet);
        Species species = petService.getSpecies(pet.getSpeciesId());

        StringBuilder sb = new StringBuilder();
        sb.append("🐾 ").append(pet.getName()).append("\n");
        if (!pet.getName().startsWith(species.getName())) sb.append("🏷 Вид: ").append(species.getName()).append("\n");
        sb.append("🔮 Редкость: ").append(species.getRarity().getDisplayName()).append("\n");
        sb.append("📜 Описание: ").append(species.getDescription()).append("\n\n");

        sb.append("📊 Уровень: ").append(pet.getLevel()).append(", Опыт: ").append(pet.getExperience()).append("/100\n");
        sb.append("❤️ HP: ").append(pet.getCurrentHp()).append("/").append(pet.getMaxHp()).append("\n");

        // Если голоден, визуально показываем штраф
        if (pet.getSatiety() == 0) {
            sb.append("⚔️ Атака: ").append(pet.getAttack()).append(" (-30%) | 🛡️ Защита: ").append(pet.getDefense()).append(" (-30%)\n");
            sb.append("⚠️ *Питомец голоден! Характеристики снижены.*\n\n");
        } else {
            sb.append("⚔️ Атака: ").append(pet.getAttack()).append(" | 🛡️ Защита: ").append(pet.getDefense()).append("\n\n");
        }

        if (species.hasPassive()) {
            sb.append("✨ Пассивка: ").append(species.getPassiveName()).append(" (").append(species.getPassiveDescription()).append(")\n\n");
        }

        sb.append("🍖 Сытость: ").append(pet.getSatiety()).append("/100\n");
        sb.append("😊 Счастье: ").append(pet.getHappiness()).append("/100\n\n");

        // Предупреждение об отказе от боя
        if (pet.getSatiety() == 0 && pet.getHappiness() == 0) {
            sb.append("🚫 *Питомец в депрессии и отказывается драться!*\n\n");
        }

        if (pet.isOnArena()) {
            sb.append("🏟️ НА АРЕНЕ (не может драться/есть)\n\n");
        }

        sb.append("🧬 Черты:\n");
        for (String trait : pet.getTraits()) sb.append("• ").append(trait).append("\n");

        return new CommandResponse(sb.toString(), species, KeyboardFactory.petActions(pet.getId(), false));
    }
}