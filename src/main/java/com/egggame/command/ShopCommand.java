package com.egggame.command;

import com.egggame.model.Player;

public class ShopCommand implements Command {
    @Override
    public String getName() {
        return "/shop";
    }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        String text = "🏪 Добро пожаловать в магазин!\n" +
                "Твой баланс: " + player.getCoins() + " монет.\n\n" +
                "Доступные товары:\n" +
                "🥚 Яйцо — 20 монет.";

        return new CommandResponse(text, null, KeyboardFactory.shopMenu());
    }
}