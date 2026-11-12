package com.egggame.command;

import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.service.ArenaService;
import com.egggame.service.PetService;

public class ArenaCommand implements Command {
    private final PetService petService;
    private final ArenaService arenaService;

    public ArenaCommand(PetService petService, ArenaService arenaService) {
        this.petService = petService;
        this.arenaService = arenaService;
    }

    @Override public String getName() { return "/arena"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 1) {
            return new CommandResponse("🏟️ Команды арены:\n/arena send <питомец> <часы>\n/arena status <питомец>\n/arena claim <питомец>", null);
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("send")) {
            if (args.length < 3) return new CommandResponse("❌ Формат: /arena send <имя/номер> <часы>", null);
            Pet pet = petService.findPet(player, args[1]);
            if (pet == null) return new CommandResponse("❌ Питомец не найден.", null);

            int hours;
            try { hours = Integer.parseInt(args[2]); } catch (Exception e) { return new CommandResponse("❌ Часы должны быть числом.", null); }

            petService.updatePetStats(pet);
            ArenaService.ArenaResult res = arenaService.sendToArena(pet, hours);
            return new CommandResponse(res.message(), null, KeyboardFactory.combatActions(pet.getId()));
        }

        if (subCommand.equals("status")) {
            if (args.length < 2) return new CommandResponse("❌ Формат: /arena status <имя/номер>", null);
            Pet pet = petService.findPet(player, args[1]);
            if (pet == null) return new CommandResponse("❌ Питомец не найден.", null);

            ArenaService.ArenaResult res = arenaService.checkStatus(pet);
            return new CommandResponse(res.message(), null, KeyboardFactory.combatActions(pet.getId()));
        }

        if (subCommand.equals("claim")) {
            if (args.length < 2) return new CommandResponse("❌ Формат: /arena claim <имя/номер>", null);
            Pet pet = petService.findPet(player, args[1]);
            if (pet == null) return new CommandResponse("❌ Питомец не найден.", null);

            ArenaService.ArenaResult res = arenaService.claim(pet);
            return new CommandResponse(res.message(), null, KeyboardFactory.combatActions(pet.getId()));
        }

        return new CommandResponse("❓ Неизвестная подкоманда арены.", null);
    }
}