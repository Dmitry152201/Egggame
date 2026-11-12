package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Rarity;
import com.egggame.model.Species;
import com.egggame.storage.PetStorage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PetService {
    private final Random random = new Random();
    private final SpeciesData speciesData;
    private final PetStorage petStorage;
    private final GameConfig config;

    public PetService(SpeciesData speciesData, PetStorage petStorage, GameConfig config) {
        this.speciesData = speciesData;
        this.petStorage = petStorage;
        this.config = config;
    }

    public Species chooseRandomSpecies(Player player) {
        List<Species> available = speciesData.getAllSpecies().stream().filter(s -> !player.hasSpecies(s.getId())).collect(Collectors.toList());
        if (available.isEmpty()) return null;
        Map<Rarity, List<Species>> byRarity = available.stream().collect(Collectors.groupingBy(Species::getRarity));
        int totalWeight = byRarity.keySet().stream().mapToInt(Rarity::getDropChancePercent).sum();
        int roll = random.nextInt(totalWeight);
        Rarity target = null;
        for (Rarity r : byRarity.keySet()) {
            roll -= r.getDropChancePercent();
            if (roll < 0) { target = r; break; }
        }
        return byRarity.get(target).get(random.nextInt(byRarity.get(target).size()));
    }

    public Pet createPet(Player player, String speciesId) {
        Species species = getSpecies(speciesId);
        List<Pet> currentPets = getPlayerPets(player);
        String defaultName = species.getName() + " #" + (currentPets.size() + 1);

        List<String> traits = new ArrayList<>();
        List<String> availableTraits = species.getAvailableTraits();
        if (availableTraits != null && !availableTraits.isEmpty()) {
            List<String> shuffled = new ArrayList<>(availableTraits);
            Collections.shuffle(shuffled);
            traits.addAll(shuffled.subList(0, Math.min(2, shuffled.size())));
        }
        Pet pet = petStorage.createPet(player.getTelegramId(), species, defaultName, traits);
        player.addPetId(pet.getId());
        return pet;
    }

    public Pet findPet(Player player, String query) {
        List<Pet> pets = getPlayerPets(player);
        for (Pet pet : pets) if (pet.getId().equalsIgnoreCase(query)) return pet;
        String cleanQuery = query.startsWith("#") ? query.substring(1) : query;
        try {
            int index = Integer.parseInt(cleanQuery);
            if (index > 0 && index <= pets.size()) return pets.get(index - 1);
        } catch (NumberFormatException ignored) {}
        for (Pet pet : pets) if (pet.getName().equalsIgnoreCase(query)) return pet;
        for (Pet pet : pets) if (pet.getName().toLowerCase().startsWith(query.toLowerCase() + " ")) return pet;
        return null;
    }

    /**
     * Выдает опыт питомцу и повышает уровень, если нужно.
     * Возвращает строку лога о повышении.
     */
    public String addExperience(Pet pet, int xp) {
        pet.setExperience(pet.getExperience() + xp);
        StringBuilder levelUpLog = new StringBuilder();

        while (pet.getExperience() >= 100) {
            pet.setExperience(pet.getExperience() - 100);
            pet.setLevel(pet.getLevel() + 1);
            Species species = getSpecies(pet.getSpeciesId());
            pet.recalculateStats(species);
            pet.setCurrentHp(pet.getMaxHp()); // Хилим на фул при апе
            levelUpLog.append("🎉 Уровень повышен до ").append(pet.getLevel()).append("! Характеристики увеличены.\n");
        }
        petStorage.savePet(pet);
        return levelUpLog.toString();
    }

    public void updatePetStats(Pet pet) {
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = Duration.between(pet.getLastUpdatedAt(), now).toMinutes();
        long effectiveHours = config.isDebugEnabled() ? minutesPassed : (minutesPassed / 60);
        if (effectiveHours > 0) {
            pet.setSatiety(pet.getSatiety() - (int)(effectiveHours * 5));
            if (pet.getSatiety() <= 0) pet.setHappiness(pet.getHappiness() - (int)(effectiveHours * 2));
            pet.setLastUpdatedAt(now);
            petStorage.savePet(pet);
        }
    }

    public void savePet(Pet pet) { petStorage.savePet(pet); }
    public Pet getPet(String petId) { return petStorage.getPet(petId); }
    public List<Pet> getPlayerPets(Player player) { return petStorage.getPetsByOwner(player.getTelegramId()); }
    public List<Pet> getAllPets() { return petStorage.getAllPets(); }
    public Species getSpecies(String speciesId) { return speciesData.getById(speciesId).orElse(null); }
}