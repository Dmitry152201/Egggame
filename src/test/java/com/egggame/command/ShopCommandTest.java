package com.egggame.command;

import com.egggame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopCommandTest {
    private ShopCommand shopCommand;
    private Player player;

    @BeforeEach
    void setUp() {
        shopCommand = new ShopCommand();
        player = new Player(111L, "TestUser");
        player.addCoins(50); // Даем игроку 50 монет для проверки
    }

    @Test
    void execute_returnsShopTextWithBalance() {
        CommandResponse response = shopCommand.execute(player, new String[]{});

        // Проверяем, что текст содержит приветствие магазина и правильный баланс
        assertTrue(response.text().contains("Добро пожаловать в магазин"));
        assertTrue(response.text().contains("Твой баланс: 50 монет"));
    }

    @Test
    void execute_returnsKeyboard() {
        CommandResponse response = shopCommand.execute(player, new String[]{});

        // Проверяем, что к ответу прикреплена клавиатура (кнопки)
        assertTrue(response.hasKeyboard());
    }
}