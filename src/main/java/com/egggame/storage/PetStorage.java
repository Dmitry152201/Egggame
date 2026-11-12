package com.egggame.storage;

import com.egggame.model.Pet;
import com.egggame.model.Species;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Хранилище питомцев в формате JSON.
 */
public class PetStorage {
    private final String storageFile;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final Map<String, Pet> pets = new HashMap<>();
    private int nextId = 1;

    public PetStorage() throws IOException {
        this("storageData/pets.json");
    }

    public PetStorage(String storageFile) throws IOException {
        this.storageFile = storageFile;
        load();
    }

    public void load() throws IOException {
        Path path = Path.of(storageFile);
        if (!Files.exists(path) || Files.size(path) == 0) return;

        try (Reader reader = Files.newBufferedReader(path)) {
            Type type = new TypeToken<Map<String, Pet>>() {}.getType();
            Map<String, Pet> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                pets.putAll(loaded);
                nextId = loaded.size() + 1;
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("⚠️ Ошибка загрузки питомцев: " + e.getMessage());
        }
    }

    public void save() {
        try {
            Path path = Path.of(storageFile);
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(pets, writer);
            }
        } catch (IOException e) {
            System.err.println("❌ Ошибка сохранения питомцев: " + e.getMessage());
        }
    }

    public Pet createPet(long ownerId, Species species, String name, List<String> traits) {
        String id = species.getId() + "_" + nextId++;
        Pet pet = new Pet(id, ownerId, species, name, LocalDateTime.now(), traits);
        pets.put(id, pet);
        save();
        return pet;
    }

    public Pet getPet(String petId) { return pets.get(petId); }

    public List<Pet> getPetsByOwner(long ownerId) {
        return pets.values().stream()
                .filter(p -> p.getOwnerId() == ownerId)
                .sorted((p1, p2) -> p1.getObtainedAt().compareTo(p2.getObtainedAt()))
                .collect(Collectors.toList());
    }

    public List<Pet> getAllPets() { return new java.util.ArrayList<>(pets.values()); }
    public void savePet(Pet pet) { pets.put(pet.getId(), pet); save(); }
}