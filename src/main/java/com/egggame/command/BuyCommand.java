package com.egggame.command;

import com.egggame.model.Player;
import com.egggame.service.PlayerService;

public class BuyCommand implements Command {
    private final PlayerService playerService;

    public BuyCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public String getName() {
        return "/buy";
    }

    @Override
    public CommandResponse execute(Player player, String[] args) {
        if (args.length < 1 || !args[0].equalsIgnoreCase("egg")) {
            return new CommandResponse("❌ Что купить? Доступно: /buy egg", null);
        }

        int price = 20;
        if (player.spendCoins(price)) {
            player.addEgg();
            playerService.savePlayer(player);
            return new CommandResponse("🥚 Ты купил яйцо за " + price + " монет!\nОно добавлено в инвентарь.\nТвой баланс: " + player.getCoins() + " монет.", null);
        }

        return new CommandResponse("❌ У тебя недостаточно монет. Нужно " + price + ", у тебя " + player.getCoins() + ".", null);
    }
}