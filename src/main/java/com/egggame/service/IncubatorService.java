package com.egggame.service;

import com.egggame.Config;
import com.egggame.model.Player;
import com.egggame.model.Species;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Сервис управления инкубатором.
 * Отвечает за кладку яиц, проверку готовности и вылупление питомцев.
 */
public class IncubatorService {

    /** Генератор случайных чисел */
    private final Random random = new Random();

    /** Конфигурация бота */
    private final Config config;

    /** Сервис управления питомцами */
    private final PetService petService;

    /**
     * Создаёт сервис инкубатора.
     *
     * @param config     конфигурация бота
     * @param petService сервис управления питомцами
     */
    public IncubatorService(Config config, PetService petService) {
        this.config = config;
        this.petService = petService;
    }

    /**
     * Пытается положить яйцо в инкубатор.
     * Проверяет, что инкубатор пуст, есть яйца и коллекция не собрана полностью.
     *
     * @param player игрок, кладущий яйцо
     * @return результат операции
     */
    public IncubatorResult putEgg(Player player) {
        if (!player.isIncubatorEmpty()) {
            return new IncubatorResult(false, "⚠️ Инкубатор уже занят. Сначала забери питомца командой /hatch.", null);
        }
        if (player.getEggs() <= 0) {
            return new IncubatorResult(false, " У тебя нет яиц. Получи новое командой /daily.", null);
        }

        // Выбираем вид питомца заранее
        Species species = petService.chooseRandomSpecies(player);
        if (species == null) {
            return new IncubatorResult(false, "🏆 Твоя коллекция собрана полностью!", null);
        }

        // Тратим яйцо и запоминаем, кто вылупится
        player.spendEgg();
        player.setIncubatingSpeciesId(species.getId());

        // Случайное время вылупления из диапазона
        long minSeconds = config.getIncubatorMinSeconds();
        long maxSeconds = config.getIncubatorMaxSeconds();
        long hatchSeconds = minSeconds + random.nextInt((int)(maxSeconds - minSeconds + 1));
        player.setHatchReadyAt(LocalDateTime.now().plusSeconds(hatchSeconds));

        // Формируем красивое сообщение в зависимости от режима
        String timeMessage;
        if (config.isDebugEnabled()) {
            timeMessage = config.getIncubatorMinSeconds() + " секунд";
        } else {
            long minHours = config.getIncubatorMinSeconds() / 3600;
            long maxHours = config.getIncubatorMaxSeconds() / 3600;
            timeMessage = minHours + "–" + maxHours + " часов";
        }

        return new IncubatorResult(true, "🥚 Яйцо помещено в инкубатор. Оно вылупится через " + timeMessage + ".", species);
    }

    /**
     * Пытается забрать вылупившегося питомца из инкубатора.
     *
     * @param player игрок, забирающий питомца
     * @return результат операции
     */
    public IncubatorResult hatch(Player player) {
        if (player.isIncubatorEmpty()) {
            return new IncubatorResult(false, "️ Инкубатор пуст. Сначала положи яйцо командой /incubate.", null);
        }
        if (!player.isEggReady(LocalDateTime.now())) {
            return new IncubatorResult(false, "⏳ Яйцо ещё не вылупилось. Подожди немного.", null);
        }

        // Создаём питомца и добавляем в коллекцию
        String speciesId = player.getIncubatingSpeciesId();
        petService.createPet(player, speciesId);

        // Очищаем инкубатор
        player.setIncubatingSpeciesId(null);
        player.setHatchReadyAt(null);

        Species species = petService.getSpecies(speciesId);
        return new IncubatorResult(true, null, species);
    }

    /**
     * Результат операции инкубатора.
     * Содержит флаг успеха, сообщение для игрока и (опционально) вид питомца.
     */
    public static class IncubatorResult {

        /** Флаг успешности операции */
        private final boolean success;

        /** Сообщение для игрока (null, если успех и есть вид) */
        private final String message;

        /** Вид питомца (null, если операция неуспешна) */
        private final Species species;

        /**
         * Создаёт результат с сообщением.
         *
         * @param success флаг успеха
         * @param message сообщение для игрока
         * @param species вид питомца (может быть null)
         */
        public IncubatorResult(boolean success, String message, Species species) {
            this.success = success;
            this.message = message;
            this.species = species;
        }

        /**
         * Возвращает флаг успешности операции.
         *
         * @return true, если операция успешна
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * Возвращает сообщение для игрока.
         *
         * @return текст сообщения или null
         */
        public String getMessage() {
            return message;
        }

        /**
         * Возвращает вид питомца.
         *
         * @return вид питомца или null
         */
        public Species getSpecies() {
            return species;
        }
    }
}