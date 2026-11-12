package com.egggame.command;
import com.egggame.model.Player;
import com.egggame.service.EggService;
import com.egggame.service.PlayerService;
public class DailyCommand implements Command {
    private final EggService eggService;
    private final PlayerService playerService;
    public DailyCommand(EggService eggService, PlayerService playerService) { this.eggService = eggService; this.playerService = playerService; }
    @Override public String getName() { return "/daily"; }
    @Override public CommandResponse execute(Player player, String[] args) {
        if (eggService.canGetDaily(player)) {
            eggService.grantDailyEgg(player);
            playerService.savePlayer(player);
            return new CommandResponse("🎁 Получено яйцо! Яиц: " + player.getEggs(), null, null);
        }
        return new CommandResponse("⏰ Через " + eggService.formatTimeRemaining(eggService.getDailySecondsRemaining(player)), null, null);
    }
}