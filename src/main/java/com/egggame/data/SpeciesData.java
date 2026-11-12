package com.egggame.data;

import com.egggame.model.Rarity;
import com.egggame.model.Species;
import java.util.List;
import java.util.Optional;

public class SpeciesData {
    private final List<Species> allSpecies;

    public SpeciesData() {
        this.allSpecies = List.of(
                new Species("mouse", "Сырный Мышонок", Rarity.COMMON, "Обожает сыр.", "pets/mouse.jpg",
                        List.of("Любит сыр", "Быстрый", "Трусливый", "Запасливый"), 30, 5, 1, null, null),

                new Species("dog_bird", "Песиптиц", Rarity.COMMON, "Верный пёс.", "pets/dog_bird.jpg",
                        List.of("Верный", "Громкий", "Летающий", "Защитник"), 45, 4, 3, null, null),

                new Species("chipmunk", "Полосатик", Rarity.COMMON, "Прячет орехи.", "pets/chipmunk.jpg",
                        List.of("Запасливый", "Обжора", "Шустрый", "Пушистый"), 25, 7, 1, null, null),

                new Species("mole_granny", "Бабушка Кротиха", Rarity.COMMON, "Печёт пироги.", "pets/mole_granny.jpg",
                        List.of("Ворчливый", "Добрый", "Пекарь", "Слепой"), 50, 3, 4, null, null),

                new Species("cat_fisherman", "Кот-Рыболов", Rarity.RARE, "Ходит в сапогах.", "pets/cat_fisherman.jpg",
                        List.of("Хитрый", "Рыбак", "Спокойный", "Мурлыка"), 40, 8, 2, "Хладнокровие", "Шанс 20% увернуться от атаки."),

                new Species("mole_monster", "Клыкастик", Rarity.RARE, "Любит корешки.", "pets/mole_monster.jpg",
                        List.of("Клыкастый", "Грозный", "Голодный", "Бесстрашный"), 55, 6, 5, "Вампиризм", "Шанс 30% украсть 50% нанесенного урона в HP."),

                new Species("penguin_tuxedo", "Пингвин-Аристократ", Rarity.RARE, "Одет по дресс-коду.", "pets/penguin_tuxedo.jpg",
                        List.of("Аристократ", "Важный", "Вежливый", "Холодостойкий"), 35, 10, 2, "Светский лев", "Шанс 20% нанести двойной урон."),

                new Species("shadow_bat", "Сумеречный Нетопырь", Rarity.EPIC, "Хранитель тайн.", "pets/shadow_bat.jpg",
                        List.of("Ночной", "Загадочный", "Кровосос", "Тихий"), 45, 12, 4, "Ваншот", "Шанс 3% убить противника с одного удара."),

                new Species("star_wanderers", "Звёздные Странники", Rarity.EPIC, "Упали с неба.", "pets/star_wanderers.jpg",
                        List.of("Звездный", "Космический", "Мечтатель", "Светящийся"), 50, 9, 6, "Звездная регенерация", "Восстанавливает 5 HP в начале каждого хода."),

                new Species("shadow_gentleman", "Теневой Джентльмен", Rarity.LEGENDARY, "Хозяин города.", "pets/shadow_gentleman.jpg",
                        List.of("Загадочный", "Элегантный", "Волшебник", "Богатый"), 65, 15, 8, "Волшебная трость", "Шанс 25% оглушить противника (пропуск хода).")
        );
    }

    public List<Species> getAllSpecies() { return allSpecies; }
    public Optional<Species> getById(String id) { return allSpecies.stream().filter(s -> s.getId().equals(id)).findFirst(); }
    public int getTotalCount() { return allSpecies.size(); }
}