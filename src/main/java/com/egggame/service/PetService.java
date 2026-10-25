package com.egggame.service;

import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Rarity;
import com.egggame.model.Species;
import com.egggame.storage.PetStorage;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Сервис создания питомцев и случайного выбора вида.
 * Реализует логику "лототрона": исключает уже пойманные виды
 * и пересчитывает шансы по оставшимся редкостям.
 */
public class PetService {

    /** Генератор случайных чисел */
    private final Random random = new Random();

    /** Каталог видов питомцев */
    private final SpeciesData speciesData;

    /** Хранилище питомцев */
    private final PetStorage petStorage;

    /**
     * Создаёт сервис управления питомцами.
     *
     * @param speciesData каталог видов
     * @param petStorage  хранилище питомцев
     */
    public PetService(SpeciesData speciesData, PetStorage petStorage) {
        this.speciesData = speciesData;
        this.petStorage = petStorage;
    }

    /**
     * Выбирает случайный вид для вылупления, исключая уже имеющиеся у игрока.
     * Если все виды собраны — возвращает null.
     *
     * @param player игрок, для которого выбирается вид
     * @return выбранный вид или null, если коллекция полна
     */
    public Species chooseRandomSpecies(Player player) {
        // Фильтруем только те виды, которых ещё нет у игрока
        List<Species> available = speciesData.getAllSpecies().stream()
                .filter(s -> !player.hasSpecies(s.getId()))
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            return null; // коллекция собрана полностью
        }

        // Группируем доступные виды по редкости
        Map<Rarity, List<Species>> byRarity = available.stream()
                .collect(Collectors.groupingBy(Species::getRarity));

        // Пересчитываем суммарный вес оставшихся редкостей
        int totalWeight = byRarity.keySet().stream()
                .mapToInt(Rarity::getDropChancePercent)
                .sum();

        // Бросаем "кубик"
        int roll = random.nextInt(totalWeight);
        Rarity target = null;
        for (Rarity r : byRarity.keySet()) {
            roll -= r.getDropChancePercent();
            if (roll < 0) {
                target = r;
                break;
            }
        }

        // Выбираем случайный вид из оставшихся в выпавшей редкости
        List<Species> pool = byRarity.get(target);
        return pool.get(random.nextInt(pool.size()));
    }

    /**
     * Создаёт нового питомца для игрока.
     *
     * @param player    игрок-владелец
     * @param speciesId ID вида питомца
     * @return созданный питомец
     */
    public Pet createPet(Player player, String speciesId) {
        Pet pet = petStorage.createPet(player.getTelegramId(), speciesId);
        player.addPetId(pet.getId());
        return pet;
    }

    /**
     * Возвращает питомца по его уникальному ID.
     *
     * @param petId ID питомца
     * @return питомец или null, если не найден
     */
    public Pet getPet(String petId) {
        return petStorage.getPet(petId);
    }

    /**
     * Возвращает всех питомцев конкретного игрока.
     *
     * @param player игрок-владелец
     * @return список питомцев игрока
     */
    public List<Pet> getPlayerPets(Player player) {
        return petStorage.getPetsByOwner(player.getTelegramId());
    }

    /**
     * Возвращает вид питомца по его ID.
     *
     * @param speciesId ID вида
     * @return вид или null, если не найден
     */
    public Species getSpecies(String speciesId) {
        return speciesData.getById(speciesId).orElse(null);
    }

    /**
     * Извлекает ID вида из ID питомца.
     * Формат ID питомца: "вид_номер" (например, "mouse_1").
     *
     * @param petId ID питомца
     * @return ID вида (часть до первого подчёркивания)
     */
    public String extractSpeciesId(String petId) {
        int underscoreIndex = petId.indexOf('_');
        if (underscoreIndex > 0) {
            return petId.substring(0, underscoreIndex);
        }
        return petId;
    }
}