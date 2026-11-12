package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.model.Pet;
import java.time.Duration;
import java.time.LocalDateTime;

public class ArenaService {
    private final PetService petService;
    private final GameConfig config;

    public ArenaService(PetService petService, GameConfig config) {
        this.petService = petService;
        this.config = config;
    }

    public record ArenaResult(boolean success, String message) {}

    public ArenaResult sendToArena(Pet pet, int hours) {
        if (pet.isOnArena()) return new ArenaResult(false, "❌ Питомец уже на арене!");
        if (hours <= 0 || hours > 24) return new ArenaResult(false, "❌ Время тренировки должно быть от 1 до 24 часов.");

        long realSeconds = hours * config.getArenaTimeMultiplierSeconds();
        pet.setArenaEndTime(LocalDateTime.now().plusSeconds(realSeconds));
        pet.setArenaHours(hours);
        petService.savePet(pet);

        int xp = hours * 10;
        return new ArenaResult(true, "🏟️ Ты отправил " + pet.getName() + " на арену на " + hours + " ч.\n" +
                "Он вернется с опытом (примерно " + xp + " XP).\n" +
                "До возвращения " + pet.getName() + " не может участвовать в боях или есть.");
    }

    public ArenaResult checkStatus(Pet pet) {
        if (!pet.isOnArena()) {
            if (pet.getArenaEndTime() != null) {
                return new ArenaResult(true, "🏟️ Тренировка окончена! Используй /arena claim, чтобы забрать награду.");
            }
            return new ArenaResult(false, "🏟️ Питомец не на арене.");
        }

        long realSecondsLeft = Duration.between(LocalDateTime.now(), pet.getArenaEndTime()).getSeconds();
        if (realSecondsLeft < 0) realSecondsLeft = 0;

        // Переводим реальные секунды в игровые, чтобы пользователь видел часы и минуты
        long multiplier = config.getArenaTimeMultiplierSeconds();
        long gameSecondsLeft = realSecondsLeft * (3600 / multiplier);

        long hours = gameSecondsLeft / 3600;
        long mins = (gameSecondsLeft % 3600) / 60;

        String timeStr = hours > 0 ? hours + " ч " + mins + " мин" : mins + " мин";
        return new ArenaResult(true, "🏟️ Питомец тренируется. До возвращения осталось: " + timeStr + ".");
    }

    public ArenaResult claim(Pet pet) {
        if (pet.getArenaEndTime() == null) return new ArenaResult(false, "❌ Питомец не был на арене.");
        if (pet.isOnArena()) {
            long realSecondsLeft = Duration.between(LocalDateTime.now(), pet.getArenaEndTime()).getSeconds();
            long gameSecondsLeft = realSecondsLeft * (3600 / config.getArenaTimeMultiplierSeconds());
            long mins = gameSecondsLeft / 60;
            return new ArenaResult(false, "⏳ Тренировка еще не окончена. Осталось " + mins + " мин.");
        }

        int earnedXp = pet.getArenaHours() * 10;
        pet.setArenaEndTime(null);
        pet.setArenaHours(0);

        String levelUp = petService.addExperience(pet, earnedXp);
        return new ArenaResult(true, "🏟️ Питомец вернулся с арены! Он получил " + earnedXp + " XP.\n" + levelUp);
    }
}