package com.egggame.service;

import com.egggame.model.Pet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Фоновый поток обновления статов.
 */
public class PetBackgroundWorker {
    private final PetService petService;
    private final ScheduledExecutorService scheduler;

    public PetBackgroundWorker(PetService petService) {
        this.petService = petService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /** Запуск фонового потока */
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (Pet pet : petService.getAllPets()) {
                    petService.updatePetStats(pet);
                }
            } catch (Exception e) {
                System.err.println("Ошибка в фоновом потоке: " + e.getMessage());
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
}