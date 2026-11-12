package com.egggame.service;

import com.egggame.config.GameConfig;
import com.egggame.model.Player;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Сервис выдачи яиц.
 */
public class EggService {
    private final GameConfig config;
    public EggService(GameConfig config) { this.config = config; }

    public boolean canGetDaily(Player player) {
        if (player.getLastDailyAt() == null) return true;
        long passed = ChronoUnit.SECONDS.between(player.getLastDailyAt(), LocalDateTime.now());
        return passed >= config.getDailyCooldownSeconds();
    }

    public long getDailySecondsRemaining(Player player) {
        if (player.getLastDailyAt() == null) return 0;
        long passed = ChronoUnit.SECONDS.between(player.getLastDailyAt(), LocalDateTime.now());
        return Math.max(0, config.getDailyCooldownSeconds() - passed);
    }

    public void grantDailyEgg(Player player) {
        player.addEgg();
        player.setLastDailyAt(LocalDateTime.now());
    }

    public String formatTimeRemaining(long seconds) {
        if (seconds < 60) return seconds + " сек";
        long m = seconds / 60;
        long h = m / 60;
        m = m % 60;
        return h > 0 ? h + " ч " + m + " мин" : m + " мин";
    }
}