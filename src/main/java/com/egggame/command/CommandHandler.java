package com.egggame.command;

import com.egggame.model.Player;
import com.egggame.service.PlayerService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Диспетчер команд. */
public class CommandHandler {
    private final PlayerService playerService;
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler(PlayerService playerService, List<Command> commandList) {
        this.playerService = playerService;
        for (Command cmd : commandList) commands.put(cmd.getName(), cmd);
    }

    public CommandResponse handleCommand(long chatId, String text, String username) {
        Player player = playerService.getOrCreate(chatId, username != null ? username : "Игрок");
        String[] parts = text.trim().split("\\s+");
        Command command = commands.get(parts[0].toLowerCase());
        if (command != null) {
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);
            return command.execute(player, args);
        }
        return new CommandResponse("❓ Неизвестная команда. Введи /help.", null);
    }
}