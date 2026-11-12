package com.egggame.command;

import com.egggame.model.Player;
import com.egggame.service.PlayerService;

public class StartCommand implements Command {
    private final PlayerService playerService;

    public StartCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public String getName() { return "/start"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (player.getEggs() == 0 && player.getLastDailyAt() == null) {
            player.addEgg();
            playerService.savePlayer(player);
            // Приветствие с флагом закрепления (true)
            return new CommandResponse(TutorialText.getWelcomeText(), null, null, true);
        }

        return new CommandResponse("👋 С возвращением в EGGGAME!\n\n" +
                "Не забывай забирать ежедневное яйцо через /daily и проверять своих питомцев в /pets.\n" +
                "Если забыл команды, пиши /help.", null, null);
    }
}