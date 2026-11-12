package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Species;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Сервис инкубатора.
 */
public class IncubatorService {
    private final Random random = new Random();
    private final GameConfig config;
    private final PetService petService;

    public IncubatorService(GameConfig config, PetService petService) {
        this.config = config;
        this.petService = petService;
    }

    public IncubatorResult putEgg(Player player) {
        if (!player.isIncubatorEmpty()) return new IncubatorResult(false, "⚠️ Инкубатор занят.", null, null);
        if (player.getEggs() <= 0) return new IncubatorResult(false, "🥚 У тебя нет яиц.", null, null);
        Species species = petService.chooseRandomSpecies(player);
        if (species == null) return new IncubatorResult(false, "🏆 Коллекция собрана!", null, null);

        player.spendEgg();
        player.setIncubatingSpeciesId(species.getId());
        long min = config.getIncubatorMinSeconds();
        long max = config.getIncubatorMaxSeconds();
        long delay = min + random.nextInt((int)(max - min + 1));
        player.setHatchReadyAt(LocalDateTime.now().plusSeconds(delay));

        String timeMsg = config.isDebugEnabled() ? min + " секунд" : (min/3600) + "–" + (max/3600) + " часов";
        return new IncubatorResult(true, "🥚 Яйцо помещено. Вылупится через " + timeMsg + ".", species, null);
    }

    public IncubatorResult hatch(Player player) {
        if (player.isIncubatorEmpty()) return new IncubatorResult(false, "⚠️ Инкубатор пуст.", null, null);
        if (!player.isEggReady(LocalDateTime.now())) return new IncubatorResult(false, "⏳ Яйцо ещё не вылупилось.", null, null);

        String speciesId = player.getIncubatingSpeciesId();
        Pet newPet = petService.createPet(player, speciesId);
        player.setIncubatingSpeciesId(null);
        player.setHatchReadyAt(null);
        return new IncubatorResult(true, null, petService.getSpecies(speciesId), newPet);
    }

    /** Результат операции инкубатора */
    public record IncubatorResult(boolean success, String message, Species species, Pet pet) {}
}