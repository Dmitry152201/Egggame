package com.egggame.command;

import com.egggame.model.Pet;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static InlineKeyboardMarkup petActions(String petId, boolean showCardBtn) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        // Первый ряд: Кормление и Ласка
        InlineKeyboardButton feedBtn = new InlineKeyboardButton(); feedBtn.setText("🍖 Покормить"); feedBtn.setCallbackData("feed_" + petId);
        InlineKeyboardButton patBtn = new InlineKeyboardButton(); patBtn.setText("💖 Погладить"); patBtn.setCallbackData("pat_" + petId);
        List<InlineKeyboardButton> row1 = new ArrayList<>(); row1.add(feedBtn); row1.add(patBtn);

        // Второй ряд: Бой и Арена
        InlineKeyboardButton fightBtn = new InlineKeyboardButton(); fightBtn.setText("⚔️ В бой"); fightBtn.setCallbackData("fight_" + petId);
        InlineKeyboardButton arenaBtn = new InlineKeyboardButton(); arenaBtn.setText("🏟️ Арена"); arenaBtn.setCallbackData("arena_" + petId);
        List<InlineKeyboardButton> row2 = new ArrayList<>(); row2.add(fightBtn); row2.add(arenaBtn);

        // Третий ряд: Имя, Карточка (опционально), К списку
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton renameBtn = new InlineKeyboardButton(); renameBtn.setText("✏️ Имя"); renameBtn.setCallbackData("rename_" + petId);
        row3.add(renameBtn);

        if (showCardBtn) {
            InlineKeyboardButton cardBtn = new InlineKeyboardButton(); cardBtn.setText("📄 Карточка"); cardBtn.setCallbackData("view_" + petId);
            row3.add(cardBtn);
        }

        InlineKeyboardButton listBtn = new InlineKeyboardButton(); listBtn.setText("🔙 К списку"); listBtn.setCallbackData("list_pets");
        row3.add(listBtn);

        markup.setKeyboard(List.of(row1, row2, row3));
        return markup;
    }

    public static InlineKeyboardMarkup combatActions(String petId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton cardBtn = new InlineKeyboardButton(); cardBtn.setText("📄 Карточка"); cardBtn.setCallbackData("view_" + petId);
        InlineKeyboardButton listBtn = new InlineKeyboardButton(); listBtn.setText("🔙 К списку"); listBtn.setCallbackData("list_pets");
        markup.setKeyboard(List.of(List.of(cardBtn, listBtn)));
        return markup;
    }

    public static InlineKeyboardMarkup petList(List<Pet> pets) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(String.valueOf(i + 1));
            btn.setCallbackData("view_" + pets.get(i).getId());
            currentRow.add(btn);
            if (currentRow.size() == 5) { rows.add(currentRow); currentRow = new ArrayList<>(); }
        }
        if (!currentRow.isEmpty()) rows.add(currentRow);
        markup.setKeyboard(rows);
        return markup;
    }

    public static InlineKeyboardMarkup incubatorReady() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton hatchBtn = new InlineKeyboardButton();
        hatchBtn.setText("🐣 Забрать питомца");
        hatchBtn.setCallbackData("hatch_ready");
        markup.setKeyboard(List.of(List.of(hatchBtn)));
        return markup;
    }
}