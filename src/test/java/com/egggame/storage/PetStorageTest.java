package com.egggame.storage;

import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Species;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetStorageTest {
    private PetStorage storage;
    private SpeciesData speciesData;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        // Используем временную папку, чтобы не засорять реальную базу
        storage = new PetStorage(tempDir.resolve("pets.json").toString());
        speciesData = new SpeciesData();
    }

    @Test
    void createPet_andGetPet_worksCorrectly() {
        Species species = speciesData.getById("mouse").orElseThrow();
        Pet pet = storage.createPet(123L, species, "TestPet", List.of("Trait1"));

        String petId = pet.getId();
        Pet loaded = storage.getPet(petId);

        assertNotNull(loaded);
        assertEquals("TestPet", loaded.getName());
        assertEquals(123L, loaded.getOwnerId());
        assertEquals(1, storage.getPetsByOwner(123L).size());
    }

    @Test
    void deletePet_removesFromStorage() {
        Species species = speciesData.getById("mouse").orElseThrow();
        Pet pet = storage.createPet(456L, species, "ToDelete", List.of());
        String petId = pet.getId();

        // Удаляем питомца
        storage.deletePet(petId);

        // Проверяем, что его больше нет ни по ID, ни у владельца
        assertNull(storage.getPet(petId));
        assertTrue(storage.getPetsByOwner(456L).isEmpty());
    }
}