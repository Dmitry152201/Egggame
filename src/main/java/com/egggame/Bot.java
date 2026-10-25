package com.egggame;

import com.egggame.command.CommandHandler;
import com.egggame.command.CommandResponse;
import com.egggame.model.Species;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;

/**
 * Telegram-бот EGGGAME: принимает сообщения и передаёт их обработчику команд.
 */
public class Bot extends TelegramLongPollingBot {

    /** Имя бота */
    private final String botUsername;

    /** Обработчик команд */
    private final CommandHandler commandHandler;

    /**
     * Создаёт бота.
     *
     * @param options        настройки бота
     * @param config         конфигурация
     * @param commandHandler обработчик команд
     */
    public Bot(DefaultBotOptions options, Config config, CommandHandler commandHandler) {
        super(options, config.getBotToken());
        this.botUsername = config.getBotUsername();
        this.commandHandler = commandHandler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        String username = update.getMessage().getFrom().getUserName();

        CommandResponse response = commandHandler.handleCommand(chatId, text, username);

        if (response == null) {
            return;
        }

        if (response.hasPhoto()) {
            sendPhotoMessage(chatId, response.getText(), response.getSpecies());
        } else {
            sendTextMessage(chatId, response.getText());
        }
    }

    /**
     * Отправляет текстовое сообщение.
     *
     * @param chatId ID чата
     * @param text   текст сообщения
     */
    private void sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    /**
     * Отправляет фото питомца с подписью.
     *
     * @param chatId  ID чата
     * @param caption текст подписи
     * @param species вид питомца для получения картинки
     */
    private void sendPhotoMessage(long chatId, String caption, Species species) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(String.valueOf(chatId));

        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(species.getImagePath());
        if (imageStream == null) {
            sendTextMessage(chatId, caption + "\n\n(Картинка не найдена)");
            return;
        }

        String filename = species.getId() + ".jpg";
        photo.setPhoto(new InputFile(imageStream, filename));
        photo.setCaption(caption);

        try {
            execute(photo);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки фото: " + e.getMessage());
        }
    }
}