package com.egggame.command;

import com.egggame.model.Player;
import com.egggame.service.IncubatorService;
import com.egggame.service.PlayerService;

/** Команда /incubate. Кладет яйцо в инкубатор. */
public class IncubateCommand implements Command {
    private final IncubatorService incubatorService;
    private final PlayerService playerService;

    public IncubateCommand(IncubatorService incubatorService, PlayerService playerService) {
        this.incubatorService = incubatorService;
        this.playerService = playerService;
    }

    @Override
    public String getName() { return "/incubate"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        IncubatorService.IncubatorResult result = incubatorService.putEgg(player);
        if (result.success()) playerService.savePlayer(player);
        return new CommandResponse(result.message(), null);
    }
}