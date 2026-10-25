package com.egggame.command;

import com.egggame.model.Species;

/**
 * Результат обработки команды: текст и (опционально) фото питомца.
 */
public class CommandResponse {

    /** Текст сообщения для игрока */
    private final String text;

    /** Вид питомца для отправки фото (null, если фото не нужно) */
    private final Species species;

    /**
     * Создаёт текстовый ответ без фото.
     *
     * @param text текст сообщения
     */
    public CommandResponse(String text) {
        this.text = text;
        this.species = null;
    }

    /**
     * Создаёт ответ с фото питомца.
     *
     * @param text    текст подписи к фото
     * @param species вид питомца для отправки картинки
     */
    public CommandResponse(String text, Species species) {
        this.text = text;
        this.species = species;
    }

    /**
     * Возвращает текст сообщения.
     *
     * @return текст для игрока
     */
    public String getText() {
        return text;
    }

    /**
     * Возвращает вид питомца для отправки фото.
     *
     * @return вид питомца или null, если фото не нужно
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Проверяет, нужно ли отправлять фото.
     *
     * @return true, если есть вид питомца для отправки
     */
    public boolean hasPhoto() {
        return species != null;
    }
}