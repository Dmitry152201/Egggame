package com.egggame.model;

import java.time.LocalDateTime;

/**
 * Конкретный экземпляр питомца у игрока.
 * В отличие от Species (шаблона вида), Pet — это живой питомец
 * с уникальным id, уровнем и датой получения.
 */
public class Pet {

    /** Уникальный идентификатор питомца (формат: "вид_номер") */
    private final String id;

    /** Telegram ID владельца */
    private final long ownerId;

    /** ID вида питомца (ссылка на Species) */
    private final String speciesId;

    /** Уровень питомца */
    private int level;

    /** Дата и время получения питомца */
    private final LocalDateTime obtainedAt;

    /**
     * Создаёт экземпляр питомца.
     *
     * @param id         уникальный идентификатор питомца
     * @param ownerId    Telegram ID владельца
     * @param speciesId  ID вида питомца
     * @param level      начальный уровень
     * @param obtainedAt дата и время получения
     */
    public Pet(String id, long ownerId, String speciesId, int level, LocalDateTime obtainedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.speciesId = speciesId;
        this.level = level;
        this.obtainedAt = obtainedAt;
    }

    /**
     * Возвращает уникальный идентификатор питомца.
     *
     * @return id питомца
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает Telegram ID владельца.
     *
     * @return id владельца
     */
    public long getOwnerId() {
        return ownerId;
    }

    /**
     * Возвращает ID вида питомца.
     *
     * @return id вида
     */
    public String getSpeciesId() {
        return speciesId;
    }

    /**
     * Возвращает уровень питомца.
     *
     * @return текущий уровень
     */
    public int getLevel() {
        return level;
    }

    /**
     * Устанавливает уровень питомца.
     *
     * @param level новый уровень
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Возвращает дату и время получения питомца.
     *
     * @return момент получения
     */
    public LocalDateTime getObtainedAt() {
        return obtainedAt;
    }
}