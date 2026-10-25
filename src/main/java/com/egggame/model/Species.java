package com.egggame.model;

/**
 * Шаблон вида питомца из справочника.
 * Описывает вид, который может вылупиться из яйца: имя, редкость,
 * описание и картинку. Не является экземпляром в коллекции игрока:
 * в коллекции хранится только id шаблона.
 */
public class Species {

    /** Уникальный идентификатор шаблона, например "mouse" */
    private final String id;

    /** Название вида для игрока, например "Сырный Мышонок" */
    private final String name;

    /** Редкость питомца данного вида */
    private final Rarity rarity;

    /** Короткое описание вида для карточки питомца */
    private final String description;

    /** Путь к картинке вида внутри resources */
    private final String imagePath;

    /**
     * Создаёт шаблон вида.
     *
     * @param id          уникальный идентификатор шаблона
     * @param name        название вида для игрока
     * @param rarity      редкость питомца
     * @param description описание вида
     * @param imagePath   путь к картинке в resources
     */
    public Species(String id, String name, Rarity rarity, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.imagePath = imagePath;
    }

    /**
     * Возвращает уникальный идентификатор шаблона.
     *
     * @return id шаблона, например "mouse"
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает название вида для игрока.
     *
     * @return название вида, например "Песиптиц"
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает редкость вида.
     *
     * @return редкость питомца
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * Возвращает описание вида.
     *
     * @return текст описания для карточки
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает путь к картинке вида.
     *
     * @return путь внутри resources, например "pets/mouse.jpg"
     */
    public String getImagePath() {
        return imagePath;
    }
}