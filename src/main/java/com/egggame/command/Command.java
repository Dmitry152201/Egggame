package com.egggame.command;

import com.egggame.model.Player;

/** Интерфейс команды (паттерн Command). */
public interface Command {
    String getName();
    CommandResponse execute(Player player, String[] args);
}