package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Species;
import com.egggame.service.IncubatorService;
import com.egggame.service.PlayerService;

public class HatchCommand implements Command {
    private final IncubatorService incubatorService;
    private final PlayerService playerService;

    public HatchCommand(IncubatorService incubatorService, PlayerService playerService) {
        this.incubatorService = incubatorService;
        this.playerService = playerService;
    }

    @Override
    public String getName() { return "/hatch"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        IncubatorService.IncubatorResult result = incubatorService.hatch(player);

        if (result.success() && result.pet() != null) {
            playerService.savePlayer(player);

            Pet pet = result.pet();
            Species species = result.species();

            StringBuilder traitsStr = new StringBuilder();
            for (String trait : pet.getTraits()) {
                traitsStr.append("• ").append(trait).append("\n");
            }

            StringBuilder caption = new StringBuilder();
            caption.append("🎉 Из яйца вылупился новый питомец!\n\n");
            caption.append("✨ Имя: ").append(pet.getName()).append("\n");
            if (!pet.getName().startsWith(species.getName())) {
                caption.append("🏷 Вид: ").append(species.getName()).append("\n");
            }
            caption.append("🔮 Редкость: ").append(species.getRarity().getDisplayName()).append("\n");
            caption.append("📜 Описание: ").append(species.getDescription()).append("\n\n");

            caption.append("📊 Уровень: ").append(pet.getLevel()).append(", Опыт: ").append(pet.getExperience()).append("/100\n");
            caption.append("❤️ HP: ").append(pet.getCurrentHp()).append("/").append(pet.getMaxHp()).append("\n");
            caption.append("⚔️ Атака: ").append(pet.getAttack()).append(" | 🛡️ Защита: ").append(pet.getDefense()).append("\n\n");

            if (species.hasPassive()) {
                caption.append("✨ Пассивка: ").append(species.getPassiveName()).append(" (").append(species.getPassiveDescription()).append(")\n\n");
            }

            caption.append("🧬 Врожденные черты:\n").append(traitsStr.toString()).append("\n");
            caption.append("🍖 Сытость: ").append(pet.getSatiety()).append("/100\n");
            caption.append("😊 Счастье: ").append(pet.getHappiness()).append("/100\n\n");
            caption.append("Он добавлен в твою коллекцию. Посмотреть всех: /pets\n");
            caption.append("💡 Не нравится авто-имя? Используй /rename <номер/имя> <новое_имя>!");

            return new CommandResponse(caption.toString(), species, KeyboardFactory.petActions(pet.getId(), false));
        }

        return new CommandResponse(result.message(), null);
    }
}