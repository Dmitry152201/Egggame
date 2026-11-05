package com.egggame.command;

import com.egggame.config.Config;
import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Species;
import com.egggame.service.EggService;
import com.egggame.service.IncubatorService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Обработчик команд бота EGGGAME.
 */
public class CommandHandler {

    private final PlayerService playerService;
    private final EggService eggService;
    private final IncubatorService incubatorService;
    private final PetService petService;
    private final SpeciesData speciesData;
    private final Config config;

    public CommandHandler(PlayerService playerService, EggService eggService,
                          IncubatorService incubatorService, PetService petService,
                          SpeciesData speciesData, Config config) {
        this.playerService = playerService;
        this.eggService = eggService;
        this.incubatorService = incubatorService;
        this.petService = petService;
        this.speciesData = speciesData;
        this.config = config;
    }

    public CommandResponse handleCommand(long chatId, String text, String username) {
        Player player = playerService.getOrCreate(chatId, username != null ? username : "Игрок");

        String trimmedText = text.trim();
        int spaceIndex = trimmedText.indexOf(' ');
        String command = (spaceIndex == -1) ? trimmedText.toLowerCase() : trimmedText.substring(0, spaceIndex).toLowerCase();
        String args = (spaceIndex == -1) ? "" : trimmedText.substring(spaceIndex).trim();

        switch (command) {
            case "/start":
                if (player.getEggs() == 0 && player.getLastDailyAt() == null) {
                    player.addEgg();
                    playerService.savePlayer(player);
                }
                return new CommandResponse("🐣 Привет! Это бот EGGGAME.\n\nТебе выдано первое яйцо! Используй /incubate, чтобы положить его в инкубатор. Или /help для списка команд.");

            case "/help":
                return new CommandResponse("📜 Список команд EGGGAME:\n\n" +
                        "/start — приветствие и регистрация\n" +
                        "/help — список команд\n" +
                        "/profile — профиль игрока\n" +
                        "/daily — получить ежедневное яйцо\n" +
                        "/incubator — состояние инкубатора\n" +
                        "/incubate — положить яйцо в инкубатор\n" +
                        "/hatch — забрать питомца из инкубатора\n" +
                        "/pets — список питомцев\n" +
                        "/pet <номер или имя> — карточка питомца\n" +
                        "/feed <номер или имя> — покормить (тратит 1 яйцо)\n" +
                        "/pat <номер или имя> — погладить");

            case "/profile":
                List<Pet> allPets = petService.getPlayerPets(player);
                int avgHappiness = 0;
                if (!allPets.isEmpty()) {
                    int totalHappy = 0;
                    for (Pet p : allPets) {
                        petService.updatePetStats(p);
                        totalHappy += p.getHappiness();
                    }
                    avgHappiness = totalHappy / allPets.size();
                }
                return new CommandResponse("👤 Профиль:\n🥚 Яиц: " + player.getEggs() +
                        "\n🐾 Питомцев: " + allPets.size() + "\n😊 Среднее счастье: " + avgHappiness + "/100");

            case "/daily":
                if (eggService.canGetDaily(player)) {
                    eggService.grantDailyEgg(player);
                    playerService.savePlayer(player);
                    return new CommandResponse("🎁 Получено ежедневное яйцо! Яиц: " + player.getEggs());
                } else {
                    long remaining = eggService.getDailySecondsRemaining(player);
                    return new CommandResponse("⏰ Следующее яйцо через " + eggService.formatTimeRemaining(remaining));
                }

            case "/incubator":
                if (player.isIncubatorEmpty()) {
                    return new CommandResponse("♨️ Инкубатор пуст. Используй /incubate.");
                } else if (player.isEggReady(LocalDateTime.now())) {
                    return new CommandResponse("🐣 Яйцо вылупилось! Используй /hatch.");
                } else {
                    return new CommandResponse("♨️ В инкубаторе яйцо. Оно скоро вылупится.");
                }

            case "/incubate":
                IncubatorService.IncubatorResult incubateResult = incubatorService.putEgg(player);
                if (incubateResult.isSuccess()) {
                    playerService.savePlayer(player);
                }
                return new CommandResponse(incubateResult.getMessage());

            case "/hatch":
                IncubatorService.IncubatorResult hatchResult = incubatorService.hatch(player);
                if (hatchResult.isSuccess() && hatchResult.getSpecies() != null) {
                    playerService.savePlayer(player);
                    Species species = hatchResult.getSpecies();
                    List<Pet> pets = petService.getPlayerPets(player);
                    Pet newPet = pets.get(pets.size() - 1);

                    StringBuilder caption = new StringBuilder();
                    caption.append("🎉 Из яйца вылупился питомец!\n\n");
                    caption.append("🏷 Вид: ").append(species.getName()).append("\n");
                    caption.append("🔮 Редкость: ").append(species.getRarity().getDisplayName()).append("\n");
                    caption.append("🍖 Сытость: ").append(newPet.getSatiety()).append("/100\n");
                    caption.append("😊 Счастье: ").append(newPet.getHappiness()).append("/100\n\n");
                    caption.append("🧬 Черты:\n");
                    for (String trait : newPet.getTraits()) {
                        caption.append("• ").append(trait).append("\n");
                    }

                    return new CommandResponse(caption.toString(), species);
                }
                return new CommandResponse(hatchResult.getMessage());

            case "/pets":
                List<Pet> pets = petService.getPlayerPets(player);
                if (pets.isEmpty()) {
                    return new CommandResponse("🐾 Коллекция пуста. Используй /incubate и /hatch!");
                }
                StringBuilder sb = new StringBuilder("🐾 Твои питомцы:\n\n");
                for (int i = 0; i < pets.size(); i++) {
                    Species s = petService.getSpecies(pets.get(i).getSpeciesId());
                    sb.append(i + 1).append(". ").append(s.getName())
                            .append(" (").append(s.getRarity().getDisplayName()).append(")\n");
                }
                sb.append("\nИспользуй /pet <номер> или /pet <имя> для подробностей.");
                return new CommandResponse(sb.toString());

            case "/pet":
                if (args.isEmpty()) return new CommandResponse("❌ Укажи номер или имя: /pet <номер/имя>");
                Pet pet = petService.findPet(player, args);
                if (pet == null) return new CommandResponse("❌ Питомец не найден. Проверь номер в /pets");
                return getPetCard(pet);

            case "/feed":
                if (args.isEmpty()) return new CommandResponse("❌ Укажи номер или имя: /feed <номер/имя>");
                Pet feedPet = petService.findPet(player, args);
                if (feedPet == null) return new CommandResponse("❌ Питомец не найден.");
                if (player.getEggs() <= 0) return new CommandResponse("❌ У тебя нет яиц. Используй /daily.");

                petService.updatePetStats(feedPet);
                int oldSat = feedPet.getSatiety();
                feedPet.setSatiety(oldSat + 25);
                player.spendEgg();
                playerService.savePlayer(player);
                petService.savePet(feedPet);

                return new CommandResponse("🍖 Ты покормил " + petService.getSpecies(feedPet.getSpeciesId()).getName() +
                        ". Сытость: " + oldSat + " → " + feedPet.getSatiety() + ".\nЯиц осталось: " + player.getEggs());

            case "/pat":
                if (args.isEmpty()) return new CommandResponse("❌ Укажи номер или имя: /pat <номер/имя>");
                Pet patPet = petService.findPet(player, args);
                if (patPet == null) return new CommandResponse("❌ Питомец не найден.");

                petService.updatePetStats(patPet);
                if (patPet.getLastPatAt() != null) {
                    long since = Duration.between(patPet.getLastPatAt(), LocalDateTime.now()).getSeconds();
                    if (since < config.getPatCooldownSeconds()) {
                        return new CommandResponse("⏳ Питомец еще не готов к ласке. Подожди " + (config.getPatCooldownSeconds() - since) + " сек.");
                    }
                }
                int oldHap = patPet.getHappiness();
                patPet.setHappiness(oldHap + 5);
                patPet.setLastPatAt(LocalDateTime.now());
                petService.savePet(patPet);

                return new CommandResponse("💖 Ты погладил " + petService.getSpecies(patPet.getSpeciesId()).getName() +
                        ". Счастье: " + oldHap + " → " + patPet.getHappiness() + ".");

            default:
                return new CommandResponse("❓ Неизвестная команда. Введи /help.");
        }
    }

    private CommandResponse getPetCard(Pet pet) {
        petService.updatePetStats(pet);
        Species s = petService.getSpecies(pet.getSpeciesId());
        StringBuilder sb = new StringBuilder();
        sb.append("🐾 Карточка питомца:\n\n");
        sb.append("🏷 Вид: ").append(s.getName()).append("\n");
        sb.append("🔮 Редкость: ").append(s.getRarity().getDisplayName()).append("\n");
        sb.append("📜 Описание: ").append(s.getDescription()).append("\n\n");
        sb.append("🍖 Сытость: ").append(pet.getSatiety()).append("/100\n");
        sb.append("😊 Счастье: ").append(pet.getHappiness()).append("/100\n\n");
        sb.append("🧬 Черты:\n");
        for (String trait : pet.getTraits()) {
            sb.append("• ").append(trait).append("\n");
        }
        return new CommandResponse(sb.toString(), s);
    }
}