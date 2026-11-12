package com.egggame.command;

import com.egggame.model.Species;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Результат обработки команды.
 */
public record CommandResponse(String text, Species species, ReplyKeyboard keyboard, boolean pin) {

    // Конструктор для ответов без клавиатуры и без закрепления
    public CommandResponse(String text, Species species) {
        this(text, species, null, false);
    }

    // Конструктор для ответов с клавиатурой, но без закрепления
    public CommandResponse(String text, Species species, ReplyKeyboard keyboard) {
        this(text, species, keyboard, false);
    }

    public boolean hasPhoto() { return species != null; }
    public boolean hasKeyboard() { return keyboard != null; }
}