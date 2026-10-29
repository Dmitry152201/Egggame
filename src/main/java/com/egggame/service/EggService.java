package com.egggame.service;

import com.egggame.config.Config;
import com.egggame.model.Player;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Сервис выдачи яиц и управления кулдауном /daily.
 * Проверяет, может ли игрок получить ежедневное яйцо,
 * и форматирует остаток времени для отображения.
 */
public class EggService {

    /** Конфигурация бота */
    private final Config config;

    /**
     * Создаёт сервис управления яйцами.
     *
     * @param config конфигурация бота
     */
    public EggService(Config config) {
        this.config = config;
    }

    /**
     * Проверяет, может ли игрок получить ежедневное яйцо.
     *
     * @param player игрок для проверки
     * @return true, если кулдаун истёк или игрок ещё не получал яйцо
     */
    public boolean canGetDaily(Player player) {
        if (player.getLastDailyAt() == null) {
            return true;
        }
        long secondsPassed = ChronoUnit.SECONDS.between(player.getLastDailyAt(), LocalDateTime.now());
        return secondsPassed >= config.getDailyCooldownSeconds();
    }

    /**
     * Возвращает количество секунд до следующего получения /daily.
     *
     * @param player игрок для проверки
     * @return оставшиеся секунды (0, если можно получить сейчас)
     */
    public long getDailySecondsRemaining(Player player) {
        if (player.getLastDailyAt() == null) {
            return 0;
        }
        long secondsPassed = ChronoUnit.SECONDS.between(player.getLastDailyAt(), LocalDateTime.now());
        long remaining = config.getDailyCooldownSeconds() - secondsPassed;
        return Math.max(0, remaining);
    }

    /**
     * Выдаёт ежедневное яйцо игроку.
     *
     * @param player игрок, получающий яйцо
     */
    public void grantDailyEgg(Player player) {
        player.addEgg();
        player.setLastDailyAt(LocalDateTime.now());
    }

    /**
     * Форматирует количество секунд в человекочитаемый вид.
     *
     * @param seconds количество секунд
     * @return строка вида "5 сек", "10 мин", "2 ч 30 мин"
     */
    public String formatTimeRemaining(long seconds) {
        if (seconds < 60) {
            return seconds + " сек";
        }
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        if (hours > 0) {
            return hours + " ч " + minutes + " мин";
        }
        return minutes + " мин";
    }
}