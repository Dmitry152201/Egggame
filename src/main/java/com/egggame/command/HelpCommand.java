package com.egggame.command;

import com.egggame.model.Player;

public class HelpCommand implements Command {
    @Override
    public String getName() { return "/help"; }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        // Чистый справочник без приветствия
        return new CommandResponse(TutorialText.getHelpText(), null, null);
    }
}