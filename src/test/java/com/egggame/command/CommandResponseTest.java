package com.egggame.command;

import com.egggame.model.Rarity;
import com.egggame.model.Species;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommandResponseTest {

    @Test
    void hasPhoto_nullSpecies_returnsFalse() {
        CommandResponse response = new CommandResponse("Текст", null);
        assertFalse(response.hasPhoto());
        assertFalse(response.pin()); // Проверяем, что по умолчанию не закрепляется
    }

    @Test
    void hasPhoto_withSpecies_returnsTrue() {
        Species species = new Species(
                "mouse", "Мышь", Rarity.COMMON, "Описание", "path", List.of(),
                30, 5, 1, null, null
        );
        CommandResponse response = new CommandResponse("Текст", species);
        assertTrue(response.hasPhoto());
    }

    @Test
    void pin_true_returnsTrue() {
        // Создаем ответ с закреплением
        CommandResponse response = new CommandResponse("Текст", null, null, true);
        assertTrue(response.pin());
    }
}