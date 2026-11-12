package com.egggame.service;

import com.egggame.model.Pet;
import com.egggame.model.Species;
import java.util.Random;

public class CombatService {
    private final Random random = new Random();
    private final PetService petService;

    public CombatService(PetService petService) { this.petService = petService; }
    public record CombatResult(boolean victory, String log, int xpGained) {}

    public CombatResult fight(Pet pet, int enemyLevel) {
        if (pet.getSatiety() == 0 && pet.getHappiness() == 0) {
            return new CombatResult(false, "❌ " + pet.getName() + " слишком голоден и несчастен. Он отказывается сражаться. Покорми и погладь его!", 0);
        }

        Species species = petService.getSpecies(pet.getSpeciesId());
        StringBuilder log = new StringBuilder();

        // Применяем дебафф голода
        int currentAtk = pet.getAttack();
        int currentDef = pet.getDefense();

        if (pet.getSatiety() == 0) {
            currentAtk = (int) (currentAtk * 0.7);
            currentDef = (int) (currentDef * 0.7);
            log.append("⚠️ ").append(pet.getName()).append(" голоден! Его Атака и Защита снижены на 30%.\n\n");
        }

        // Генерация врага
        int enemyMaxHp = 30 + (enemyLevel * 10);
        int enemyHp = enemyMaxHp;
        int enemyAtk = 4 + (enemyLevel * 2);
        int enemyDef = 1 + (enemyLevel * 1);

        log.append("⚔️ Твой ").append(pet.getName()).append(" вступает в бой с Противником ").append(enemyLevel).append("-го уровня!\n\n");

        // --- НОВОЕ: Вывод статов питомца ---
        log.append("🐾 Твои характеристики:\n");
        log.append("❤️ HP: ").append(pet.getCurrentHp()).append("/").append(pet.getMaxHp()).append("\n");
        log.append("⚔️ Атака: ").append(currentAtk).append("\n");
        log.append("🛡️ Защита: ").append(currentDef).append("\n\n");

        // Статы врага
        log.append("👹 Характеристики Противника:\n");
        log.append("❤️ HP: ").append(enemyHp).append("/").append(enemyMaxHp).append("\n");
        log.append("⚔️ Атака: ").append(enemyAtk).append("\n");
        log.append("🛡️ Защита: ").append(enemyDef).append("\n\n");

        int petHp = pet.getCurrentHp();
        int turn = 1;
        boolean enemyStunned = false;

        while (petHp > 0 && enemyHp > 0 && turn <= 20) {
            log.append("Ход ").append(turn).append(": ");

            // --- Ход питомца ---
            int petDamage = Math.max(1, currentAtk - enemyDef);
            String passiveText = "";

            if (species.hasPassive()) {
                if (species.getPassiveName().equals("Вампиризм") && random.nextInt(100) < 30) {
                    int heal = petDamage / 2;
                    petHp = Math.min(pet.getMaxHp(), petHp + heal);
                    passiveText = " (Сработал Вампиризм! +" + heal + " HP)";
                } else if (species.getPassiveName().equals("Светский лев") && random.nextInt(100) < 20) {
                    petDamage *= 2;
                    passiveText = " (Сработал Светский лев! Двойной урон!)";
                } else if (species.getPassiveName().equals("Ваншот") && random.nextInt(100) < 3) {
                    enemyHp = 0;
                    passiveText = " (Сработал Ваншот! Мгновенная смерть!)";
                } else if (species.getPassiveName().equals("Волшебная трость") && random.nextInt(100) < 25) {
                    enemyStunned = true;
                    passiveText = " (Сработала Волшебная трость! Враг оглушен!)";
                }
            }

            if (enemyHp > 0) {
                enemyHp = Math.max(0, enemyHp - petDamage);
                log.append("Твой ").append(pet.getName()).append(" атакует! Наносит ").append(petDamage).append(" урона.").append(passiveText);
                log.append(" У Врага осталось ").append(enemyHp).append(" HP.\n");
            }

            if (enemyHp <= 0) break;

            // --- Ход врага ---
            log.append("Ход ").append(turn).append(": ");

            if (enemyStunned) {
                log.append("Враг оглушен и пропускает ход!\n");
                enemyStunned = false;
            } else {
                int enemyDamage = Math.max(1, enemyAtk - currentDef);
                String defenseText = "";

                if (species.hasPassive()) {
                    if (species.getPassiveName().equals("Хладнокровие") && random.nextInt(100) < 20) {
                        enemyDamage = 0;
                        defenseText = " (Сработало Хладнокровие! Уклонение!)";
                    }
                }

                if (enemyDamage > 0) {
                    petHp = Math.max(0, petHp - enemyDamage);
                    log.append("Враг атакует в ответ! Наносит ").append(enemyDamage).append(" урона.").append(defenseText);
                    log.append(" У Тебя осталось ").append(petHp).append(" HP.\n");
                } else {
                    log.append("Враг атакует, но промахивается!").append(defenseText).append("\n");
                }
            }

            if (species.hasPassive() && species.getPassiveName().equals("Звездная регенерация")) {
                petHp = Math.min(pet.getMaxHp(), petHp + 5);
                log.append("✨ Звездная регенерация восстановила 5 HP.\n");
            }
            turn++;
        }

        pet.setCurrentHp(petHp);
        petService.savePet(pet);

        if (petHp > 0 && enemyHp <= 0) {
            int xpGain = 5 * enemyLevel;
            String levelUpLog = petService.addExperience(pet, xpGain);
            log.append("\n🏆 Победа! Твой ").append(pet.getName()).append(" получает ").append(xpGain).append(" XP.\n");
            log.append("Теперь у ").append(pet.getName()).append(" ").append(pet.getExperience()).append("/100 опыта.\n");
            log.append(levelUpLog);
            return new CombatResult(true, log.toString(), xpGain);
        } else {
            log.append("\n💀 Поражение... Твой питомец пал в бою. HP восстановлено до 1.\n");
            pet.setCurrentHp(1);
            petService.savePet(pet);
            return new CombatResult(false, log.toString(), 0);
        }
    }
}