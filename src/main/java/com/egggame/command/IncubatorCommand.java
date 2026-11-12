package com.egggame.command;
import com.egggame.model.Player;
import java.time.LocalDateTime;
public class IncubatorCommand implements Command {
    @Override public String getName() { return "/incubator"; }
    @Override public CommandResponse execute(Player player, String[] args) {
        if (player.isIncubatorEmpty()) return new CommandResponse("♨️ Инкубатор пуст. Используй /incubate.", null, null);
        if (player.isEggReady(LocalDateTime.now())) return new CommandResponse("🐣 Яйцо вылупилось!", null, KeyboardFactory.incubatorReady());
        return new CommandResponse("♨️ Яйцо скоро вылупится.", null, null);
    }
}