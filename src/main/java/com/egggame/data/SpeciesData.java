package com.egggame.data;

import com.egggame.model.Rarity;
import com.egggame.model.Species;

import java.util.List;
import java.util.Optional;

/**
 * Каталог всех видов питомцев в игре.
 * Содержит статический список из 10 видов разных редкостей.
 */
public class SpeciesData {

    /** Список всех видов питомцев */
    private final List<Species> allSpecies;

    /**
     * Создаёт каталог видов.
     */
    public SpeciesData() {
        this.allSpecies = List.of(
                // Обычные (4)
                new Species("mouse", "Сырный Мышонок", Rarity.COMMON, "Обожает сыр и уютные норки.", "pets/mouse.jpg"),
                new Species("dog_bird", "Песиптиц", Rarity.COMMON, "Верный пёс и его пернатый штурман.", "pets/dog_bird.jpg"),
                new Species("chipmunk", "Полосатик", Rarity.COMMON, "Прячет орехи лучше любого банка.", "pets/chipmunk.jpg"),
                new Species("mole_granny", "Бабушка Кротиха", Rarity.COMMON, "Всегда ворчит, но печёт самые вкусные пироги.", "pets/mole_granny.jpg"),

                // Редкие (3)
                new Species("cat_fisherman", "Кот-Рыболов", Rarity.RARE, "Ходит в сапогах и знает лучшие рыбные места.", "pets/cat_fisherman.jpg"),
                new Species("mole_monster", "Клыкастик", Rarity.RARE, "Выглядит грозно, но просто любит погрызть корешки.", "pets/mole_monster.jpg"),
                new Species("penguin_tuxedo", "Пингвин-Аристократ", Rarity.RARE, "Всегда одет по строжайшему дресс-коду.", "pets/penguin_tuxedo.jpg"),

                // Эпические (2)
                new Species("shadow_bat", "Сумеречный Нетопырь", Rarity.EPIC, "Хранитель ночных тайн и мягких крыльев.", "pets/shadow_bat.jpg"),
                new Species("star_wanderers", "Звёздные Странники", Rarity.EPIC, "Пара существ, упавших прямо с ночного неба.", "pets/star_wanderers.jpg"),

                // Легендарный (1)
                new Species("shadow_gentleman", "Теневой Джентльмен", Rarity.LEGENDARY, "Загадочный хозяин ночного города с волшебной тростью.", "pets/shadow_gentleman.jpg")
        );
    }

    /**
     * Возвращает список всех видов.
     *
     * @return неизменяемый список всех видов питомцев
     */
    public List<Species> getAllSpecies() {
        return allSpecies;
    }

    /**
     * Находит вид по его уникальному идентификатору.
     *
     * @param id идентификатор вида (например, "mouse")
     * @return Optional с видом, если найден
     */
    public Optional<Species> getById(String id) {
        return allSpecies.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    /**
     * Возвращает общее количество видов в каталоге.
     *
     * @return количество видов (10)
     */
    public int getTotalCount() {
        return allSpecies.size();
    }
}