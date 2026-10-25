package com.egggame.command;

import com.egggame.data.SpeciesData;
import com.egggame.model.Pet;
import com.egggame.model.Player;
import com.egggame.model.Species;
import com.egggame.service.EggService;
import com.egggame.service.IncubatorService;
import com.egggame.service.PetService;
import com.egggame.service.PlayerService;

import java.util.List;

/**
 * Обработчик команд бота EGGGAME.
 * Принимает текст команды и возвращает ответ для отправки игроку.
 */
public class CommandHandler {

    /** Сервис управления игроками */
    private final PlayerService playerService;

    /** Сервис выдачи яиц */
    private final EggService eggService;

    /** Сервис инкубатора */
    private final IncubatorService incubatorService;

    /** Сервис управления питомцами */
    private final PetService petService;

    /** Каталог видов питомцев */
    private final SpeciesData speciesData;

    /**
     * Создаёт обработчик команд.
     *
     * @param playerService    сервис игроков
     * @param eggService       сервис яиц
     * @param incubatorService сервис инкубатора
     * @param petService       сервис питомцев
     * @param speciesData      каталог видов
     */
    public CommandHandler(PlayerService playerService, EggService eggService,
                          IncubatorService incubatorService, PetService petService,
                          SpeciesData speciesData) {
        this.playerService = playerService;
        this.eggService = eggService;
        this.incubatorService = incubatorService;
        this.petService = petService;
        this.speciesData = speciesData;
    }

    /**
     * Обрабатывает команду и возвращает ответ.
     *
     * @param chatId   Telegram ID чата
     * @param text     текст команды
     * @param username Telegram-имя игрока
     * @return ответ для отправки игроку
     */
    public CommandResponse handleCommand(long chatId, String text, String username) {
        Player player = playerService.getOrCreate(chatId, username != null ? username : "Игрок");

        switch (text) {
            case "/start":
                return new CommandResponse(" Привет! Это бот EGGGAME.\n\n" +
                        "Тут ты можешь собирать коллекцию питомцев, выбивая их из яиц.\n\n" +
                        "Используй /daily, чтобы получить первое яйцо, или /help для просмотра всех команд.");

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
                        "/pet <имя> — карточка питомца");

            case "/profile":
                int eggs = player.getEggs();
                int petsCount = petService.getPlayerPets(player).size();
                return new CommandResponse("👤 Профиль:\n\n" +
                        "🥚 Яиц: " + eggs + "\n" +
                        "🐾 Питомцев: " + petsCount);

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
                } else if (player.isEggReady(java.time.LocalDateTime.now())) {
                    return new CommandResponse("️ Яйцо вылупилось! Используй /hatch.");
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
                    String caption = "🎉 Вам выпал питомец!\n\n" +
                            "✨ Имя: " + species.getName() + "\n" +
                            "🏷 Редкость: " + species.getRarity().getDisplayName() + "\n" +
                            "📜 Описание: " + species.getDescription();
                    return new CommandResponse(caption, species);
                }
                return new CommandResponse(hatchResult.getMessage());

            case "/pets":
                List<Pet> pets = petService.getPlayerPets(player);
                if (pets.isEmpty()) {
                    return new CommandResponse("🐾 Коллекция пуста. Используй /incubate и /hatch!");
                }
                StringBuilder sb = new StringBuilder("🐾 Твои питомцы:\n\n");
                for (int i = 0; i < pets.size(); i++) {
                    Pet pet = pets.get(i);
                    Species species = petService.getSpecies(pet.getSpeciesId());
                    if (species != null) {
                        sb.append(i + 1).append(". ").append(species.getName())
                                .append(" (").append(species.getRarity().getDisplayName()).append(")\n");
                    }
                }
                sb.append("\nИспользуй /pet <имя> для подробностей.");
                return new CommandResponse(sb.toString());

            default:
                if (text.startsWith("/pet ")) {
                    String petName = text.substring(5).trim();
                    List<Pet> playerPets = petService.getPlayerPets(player);
                    Pet foundPet = null;
                    for (Pet pet : playerPets) {
                        Species species = petService.getSpecies(pet.getSpeciesId());
                        if (species != null && species.getName().equalsIgnoreCase(petName)) {
                            foundPet = pet;
                            break;
                        }
                    }
                    if (foundPet != null) {
                        Species species = petService.getSpecies(foundPet.getSpeciesId());
                        String caption = "🐾 Карточка питомца:\n\n" +
                                "✨ Имя: " + species.getName() + "\n" +
                                "🏷 Редкость: " + species.getRarity().getDisplayName() + "\n" +
                                "📜 Описание: " + species.getDescription();
                        return new CommandResponse(caption, species);
                    }
                    return new CommandResponse("❌ Питомец «" + petName + "» не найден в твоей коллекции.");
                }
                return new CommandResponse("❓ Неизвестная команда. Введи /help.");
        }
    }
}