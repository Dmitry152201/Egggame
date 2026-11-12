package com.egggame;

import com.egggame.command.CommandHandler;
import com.egggame.command.CommandResponse;
import com.egggame.command.KeyboardFactory;
import com.egggame.config.BotConfig;
import com.egggame.model.Species;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.InputStream;

public class Bot extends TelegramLongPollingBot {
    private final String botUsername;
    private final CommandHandler commandHandler;

    public Bot(DefaultBotOptions options, BotConfig config, CommandHandler commandHandler) {
        super(options, config.getBotToken());
        this.botUsername = config.getBotUsername();
        this.commandHandler = commandHandler;
    }

    @Override public String getBotUsername() { return botUsername; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String username = update.getCallbackQuery().getFrom().getUserName();

            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(update.getCallbackQuery().getId());
            try { execute(answer); } catch (TelegramApiException ignored) {}

            String commandText = "";

            if (callData.startsWith("feed_")) commandText = "/feed " + callData.substring(5);
            else if (callData.startsWith("pat_")) commandText = "/pat " + callData.substring(4);
            else if (callData.startsWith("view_")) commandText = "/pet " + callData.substring(5);
            else if (callData.equals("list_pets")) commandText = "/pets";
            else if (callData.equals("hatch_ready")) commandText = "/hatch";
            else if (callData.startsWith("rename_")) {
                String petId = callData.substring(7);
                sendTextMessage(chatId, new CommandResponse("✏️ Чтобы переименовать, напиши:\n/rename " + petId + " <новое_имя>", null, KeyboardFactory.petActions(petId, true)));
                return;
            } else if (callData.startsWith("fight_")) {
                String petId = callData.substring(6);
                sendTextMessage(chatId, new CommandResponse("⚔️ Чтобы начать бой, напиши:\n/fight " + petId + " <уровень_врага>", null, KeyboardFactory.combatActions(petId)));
                return;
            } else if (callData.startsWith("arena_")) {
                String petId = callData.substring(6);
                String text = "🏟️ **Управление ареной:**\n\n" +
                        "Отправить на тренировку:\n" +
                        "/arena send " + petId + " <часы>\n\n" +
                        "Проверить статус:\n" +
                        "/arena status " + petId + "\n\n" +
                        "Забрать награду:\n" +
                        "/arena claim " + petId;
                sendTextMessage(chatId, new CommandResponse(text, null, KeyboardFactory.petActions(petId, true)));
                return;
            }

            if (!commandText.isEmpty()) {
                CommandResponse response = commandHandler.handleCommand(chatId, commandText, username);
                sendResponse(chatId, response);
            }
            return;
        }

        if (!update.hasMessage() || !update.getMessage().hasText()) return;
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        String username = update.getMessage().getFrom().getUserName();

        CommandResponse response = commandHandler.handleCommand(chatId, text, username);
        sendResponse(chatId, response);
    }

    private void sendResponse(long chatId, CommandResponse response) {
        if (response == null) return;
        if (response.hasPhoto()) sendPhotoMessage(chatId, response);
        else {
            Message sent = sendTextMessage(chatId, response);
            if (response.pin() && sent != null) pinMessage(chatId, sent.getMessageId());
        }
    }

    private Message sendTextMessage(long chatId, CommandResponse response) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(response.text());
        if (response.hasKeyboard()) message.setReplyMarkup(response.keyboard());
        try { return execute(message); } catch (TelegramApiException e) { return null; }
    }

    private void sendPhotoMessage(long chatId, CommandResponse response) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(String.valueOf(chatId));
        Species species = response.species();
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(species.getImagePath());
        if (imageStream == null) { sendTextMessage(chatId, response); return; }
        photo.setPhoto(new InputFile(imageStream, species.getId() + ".jpg"));
        photo.setCaption(response.text());
        if (response.hasKeyboard()) photo.setReplyMarkup(response.keyboard());
        try { execute(photo); } catch (TelegramApiException e) { }
    }

    private void pinMessage(long chatId, int messageId) {
        try { execute(PinChatMessage.builder().chatId(String.valueOf(chatId)).messageId(messageId).build()); } catch (TelegramApiException e) { }
    }
}