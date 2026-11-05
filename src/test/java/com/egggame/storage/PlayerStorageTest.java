package com.egggame.storage;

import com.egggame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты хранилища игроков.
 * Проверяют сохранение, загрузку и проверку существования игрока.
 */
class PlayerStorageTest {

    private PlayerStorage playerStorage;

    /**
     * Создает чистое хранилище перед каждым тестом.
     */
    @BeforeEach
    void setUp() throws IOException {
        playerStorage = new PlayerStorage();
    }

    /**
     * Проверяет, что сохраненный игрок успешно достается из хранилища,
     * а его данные (количество яиц) не теряются (Слайд 18).
     */
    @Test
    void testSavePlayerAndGetPlayer_returnsCorrectState() {
        Player player = new Player(123456789L, "TestPlayer");
        player.setEggs(5); // Устанавливаем кастомное состояние

        playerStorage.savePlayer(player);
        Player loaded = playerStorage.getPlayer(123456789L);

        assertNotNull(loaded);
        assertEquals(5, loaded.getEggs()); // Проверяем, что состояние сохранилось
    }

    /**
     * Проверяет, что метод hasPlayer возвращает true для существующего игрока.
     */
    @Test
    void testHasPlayer_existingPlayer_returnsTrue() {
        Player player = new Player(987654321L, "TestPlayer2");
        playerStorage.savePlayer(player);

        assertTrue(playerStorage.hasPlayer(987654321L));
    }

    /**
     * Проверяет противоположный случай: hasPlayer возвращает false для несуществующего (Слайд 22).
     */
    @Test
    void testHasPlayer_nonExistingPlayer_returnsFalse() {
        // Игрока с таким ID мы не добавляли
        assertFalse(playerStorage.hasPlayer(111111111L));
    }
}