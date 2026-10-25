package com.egggame.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Игрок EGGGAME: всё его состояние в одном объекте.
 * Хранит количество яиц, время последнего /daily, состояние инкубатора
 * и список id питомцев в коллекции.
 */
public class Player {

    /** Telegram ID игрока, уникальный ключ */
    private final long telegramId;

    /** Telegram-имя игрока для профиля */
    private String username;

    /** Количество яиц в инвентаре */
    private int eggs;

    /** Время последнего получения /daily, null если ещё не получал */
    private LocalDateTime lastDailyAt;

    /** Время готовности яйца в инкубаторе, null если инкубатор пуст */
    private LocalDateTime hatchReadyAt;

    /** ID вида питомца, сидящего в яйце, null если инкубатор пуст */
    private String incubatingSpeciesId;

    /** Список ID питомцев игрока (уникальные экземпляры) */
    private final List<String> petIds = new ArrayList<>();

    /**
     * Создаёт нового игрока с пустым состоянием.
     *
     * @param telegramId Telegram ID игрока
     * @param username   Telegram-имя игрока
     */
    public Player(long telegramId, String username) {
        this.telegramId = telegramId;
        this.username = username;
    }

    /**
     * Возвращает Telegram ID игрока.
     *
     * @return уникальный Telegram ID
     */
    public long getTelegramId() {
        return telegramId;
    }

    /**
     * Возвращает Telegram-имя игрока.
     *
     * @return username игрока
     */
    public String getUsername() {
        return username;
    }

    /**
     * Обновляет Telegram-имя игрока.
     *
     * @param username новое username игрока
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает количество яиц в инвентаре.
     *
     * @return число яиц
     */
    public int getEggs() {
        return eggs;
    }

    /**
     * Устанавливает количество яиц в инвентаре.
     *
     * @param eggs новое число яиц
     */
    public void setEggs(int eggs) {
        this.eggs = eggs;
    }

    /** Добавляет одно яйцо в инвентарь. */
    public void addEgg() {
        this.eggs++;
    }

    /** Тратит одно яйцо из инвентаря (кладка в инкубатор). */
    public void spendEgg() {
        this.eggs--;
    }

    /**
     * Возвращает время последнего получения ежедневного яйца.
     *
     * @return время /daily или null, если игрок ещё не получал яйцо
     */
    public LocalDateTime getLastDailyAt() {
        return lastDailyAt;
    }

    /**
     * Фиксирует время получения ежедневного яйца.
     *
     * @param lastDailyAt время получения
     */
    public void setLastDailyAt(LocalDateTime lastDailyAt) {
        this.lastDailyAt = lastDailyAt;
    }

    /**
     * Возвращает время готовности яйца в инкубаторе.
     *
     * @return время вылупления или null, если инкубатор пуст
     */
    public LocalDateTime getHatchReadyAt() {
        return hatchReadyAt;
    }

    /**
     * Устанавливает время готовности яйца в инкубаторе.
     *
     * @param hatchReadyAt время готовности, null очищает инкубатор
     */
    public void setHatchReadyAt(LocalDateTime hatchReadyAt) {
        this.hatchReadyAt = hatchReadyAt;
    }

    /**
     * Возвращает ID вида питомца, сидящего в яйце.
     *
     * @return ID вида или null, если инкубатор пуст
     */
    public String getIncubatingSpeciesId() {
        return incubatingSpeciesId;
    }

    /**
     * Устанавливает вид питомца, сидящего в яйце.
     *
     * @param incubatingSpeciesId ID вида, null очищает инкубатор
     */
    public void setIncubatingSpeciesId(String incubatingSpeciesId) {
        this.incubatingSpeciesId = incubatingSpeciesId;
    }

    /**
     * Возвращает список ID питомцев игрока.
     *
     * @return список ID питомцев
     */
    public List<String> getPetIds() {
        return petIds;
    }

    /**
     * Добавляет ID питомца в коллекцию игрока.
     *
     * @param petId ID питомца
     */
    public void addPetId(String petId) {
        petIds.add(petId);
    }

    /**
     * Проверяет, пуст ли инкубатор.
     *
     * @return true, если в инкубаторе нет яйца
     */
    public boolean isIncubatorEmpty() {
        return hatchReadyAt == null;
    }

    /**
     * Проверяет, вылупилось ли яйцо в инкубаторе к моменту now.
     *
     * @param now текущее время
     * @return true, если яйцо готово и можно вызывать /hatch
     */
    public boolean isEggReady(LocalDateTime now) {
        return hatchReadyAt != null && !now.isBefore(hatchReadyAt);
    }

    /**
     * Проверяет, есть ли у игрока питомец данного вида.
     * Используется для запрета дубликатов.
     *
     * @param speciesId ID вида для проверки
     * @return true, если у игрока уже есть питомец этого вида
     */
    public boolean hasSpecies(String speciesId) {
        return petIds.stream()
                .anyMatch(petId -> petId.startsWith(speciesId + "_"));
    }
}