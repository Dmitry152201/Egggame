package com.egggame.storage;

import com.egggame.model.Pet;
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
 * Хранилище питомцев с сохранением в JSON-файл.
 * Использует кастомный адаптер для LocalDateTime и защищено от чтения пустых файлов.
 */
public class PetStorage {

    /** Путь к файлу хранилища */
    private static final String STORAGE_FILE = "storage/pets.json";

    /** Gson с кастомным адаптером для LocalDateTime */
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    /** Кэш питомцев в памяти */
    private final Map<String, Pet> pets = new HashMap<>();

    /** Счётчик для генерации уникальных ID */
    private int nextId = 1;

    /**
     * Создаёт хранилище и загружает данные из файла.
     */
    public PetStorage() throws IOException {
        load();
    }

    /**
     * Загружает питомцев из JSON-файла.
     */
    public void load() throws IOException {
        Path path = Path.of(STORAGE_FILE);

        // ЗАЩИТА: если файла нет или он пустой (0 байт), просто выходим
        if (!Files.exists(path) || Files.size(path) == 0) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            Type type = new TypeToken<Map<String, Pet>>() {}.getType();
            Map<String, Pet> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                pets.putAll(loaded);
                nextId = loaded.size() + 1; // Обновляем счётчик, чтобы ID не повторялись
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("⚠️ Ошибка загрузки питомцев (файл повреждён или пуст, начинаем с чистого листа): " + e.getMessage());
            // Бот не падает, а просто продолжает работу с пустой картой
        }
    }

    /**
     * Сохраняет всех питомцев в JSON-файл.
     */
    public void save() {
        try {
            Files.createDirectories(Path.of("storage"));
            try (Writer writer = Files.newBufferedWriter(Path.of(STORAGE_FILE))) {
                gson.toJson(pets, writer);
            }
        } catch (IOException e) {
            System.err.println("❌ Ошибка сохранения питомцев: " + e.getMessage());
        }
    }

    /**
     * Создаёт нового питомца и сохраняет его в хранилище.
     *
     * @param ownerId   Telegram ID владельца
     * @param speciesId ID вида питомца
     * @return созданный питомец
     */
    public Pet createPet(long ownerId, String speciesId) {
        String id = speciesId + "_" + nextId++;
        Pet pet = new Pet(id, ownerId, speciesId, 1, LocalDateTime.now());
        pets.put(id, pet);
        save();
        return pet;
    }

    /**
     * Возвращает питомца по его уникальному ID.
     *
     * @param petId ID питомца
     * @return питомец или null, если не найден
     */
    public Pet getPet(String petId) {
        return pets.get(petId);
    }

    /**
     * Возвращает всех питомцев конкретного владельца.
     *
     * @param ownerId Telegram ID владельца
     * @return список питомцев владельца
     */
    public List<Pet> getPetsByOwner(long ownerId) {
        return pets.values().stream()
                .filter(p -> p.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет данные питомца в хранилище.
     *
     * @param pet питомец для обновления
     */
    public void savePet(Pet pet) {
        pets.put(pet.getId(), pet);
        save();
    }
}