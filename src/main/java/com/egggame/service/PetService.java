package com.egggame.service;

import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Rarity;
import com.egggame.model.Species;
import com.egggame.storage.PetStorage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Сервис создания питомцев, случайного выбора вида и обновления их статов.
 * Реализует логику "лототрона" и фоновую механику голода.
 */
public class PetService {

    private final Random random = new Random();
    private final SpeciesData speciesData;
    private final PetStorage petStorage;

    /** Общий пул черт характера для всех питомцев */
    private static final List<String> ALL_TRAITS = List.of(
            "Обжора", "Соня", "Быстрый", "Трусливый", "Верный",
            "Хитрый", "Громкий", "Любит греться на солнце",
            "Боится громких звуков", "Запасливый", "Любопытный", "Ленивый", "Гей"
    );

    public PetService(SpeciesData speciesData, PetStorage petStorage) {
        this.speciesData = speciesData;
        this.petStorage = petStorage;
    }

    public Species chooseRandomSpecies(Player player) {
        List<Species> available = speciesData.getAllSpecies().stream()
                .filter(s -> !player.hasSpecies(s.getId()))
                .collect(Collectors.toList());

        if (available.isEmpty()) return null;

        Map<Rarity, List<Species>> byRarity = available.stream()
                .collect(Collectors.groupingBy(Species::getRarity));

        int totalWeight = byRarity.keySet().stream()
                .mapToInt(Rarity::getDropChancePercent)
                .sum();

        int roll = random.nextInt(totalWeight);
        Rarity target = null;
        for (Rarity r : byRarity.keySet()) {
            roll -= r.getDropChancePercent();
            if (roll < 0) {
                target = r;
                break;
            }
        }

        List<Species> pool = byRarity.get(target);
        return pool.get(random.nextInt(pool.size()));
    }

    public Pet createPet(Player player, String speciesId) {
        List<String> shuffled = new ArrayList<>(ALL_TRAITS);
        Collections.shuffle(shuffled);
        List<String> traits = shuffled.subList(0, 2);

        Pet pet = petStorage.createPet(player.getTelegramId(), speciesId, traits);
        player.addPetId(pet.getId());
        return pet;
    }

    public Pet findPet(Player player, String query) {
        List<Pet> pets = getPlayerPets(player);

        // Пытаемся найти по номеру
        try {
            int index = Integer.parseInt(query);
            if (index > 0 && index <= pets.size()) {
                return pets.get(index - 1);
            }
        } catch (NumberFormatException ignored) {
            // Это не число, ищем по имени
        }

        // Поиск по названию вида (например, "Сырный Мышонок")
        for (Pet pet : pets) {
            Species s = getSpecies(pet.getSpeciesId());
            if (s != null && s.getName().equalsIgnoreCase(query)) {
                return pet;
            }
        }
        return null;
    }

    /**
     * Ленивый пересчет статов (Фоновая механика голода).
     */
    public void updatePetStats(Pet pet) {
        LocalDateTime now = LocalDateTime.now();

        // Защита для старых питомцев (если поля нет, просто ставим текущее время)
        if (pet.getLastUpdatedAt() == null) {
            pet.setLastUpdatedAt(now);
            petStorage.savePet(pet);
            return;
        }

        long hoursPassed = Duration.between(pet.getLastUpdatedAt(), now).toHours();

        if (hoursPassed > 0) {
            pet.setSatiety(pet.getSatiety() - (int)(hoursPassed * 5));

            if (pet.getSatiety() <= 0) {
                pet.setHappiness(pet.getHappiness() - (int)(hoursPassed * 2));
            }

            pet.setLastUpdatedAt(now);
            petStorage.savePet(pet);
        }
    }

    public void savePet(Pet pet) { petStorage.savePet(pet); }
    public Pet getPet(String petId) { return petStorage.getPet(petId); }
    public List<Pet> getPlayerPets(Player player) { return petStorage.getPetsByOwner(player.getTelegramId()); }
    public Species getSpecies(String speciesId) { return speciesData.getById(speciesId).orElse(null); }
    public String extractSpeciesId(String petId) {
        int underscoreIndex = petId.indexOf('_');
        return underscoreIndex > 0 ? petId.substring(0, underscoreIndex) : petId;
    }
}