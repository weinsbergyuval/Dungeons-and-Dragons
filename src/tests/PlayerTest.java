package tests;

import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private TestPlayer player;
    private TestMessageCallback messageCallback;

    // Concrete subclass of Player for testing
    static class TestPlayer extends Player {
        public TestPlayer(String name, Position position, Health health, int attackPoints, int defensePoints) {
            super(name, '@', position, health, attackPoints, defensePoints);
        }

        @Override
        public String postLevelingUp(MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
            return super.postLevelingUp(cb, deltaHealth, deltaAttack, deltaDefense);
        }

        @Override
        public void OnGameRound() {
            // no-op for testing
        }

        @Override
        public void castAbility(MessageCallback cb, game.callbacks.ClearSightCallback cleanSightCallback, java.util.List<game.tiles.units.enemy.Enemy> enemies, Player player) {
            // no-op for testing
        }
    }

    // Simple MessageCallback mock to capture callback messages
    static class TestMessageCallback implements MessageCallback {
        private String lastMessage;

        @Override
        public void send(String message) {
            lastMessage = message;
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }

    @BeforeEach
    void setUp() {
        player = new TestPlayer("yuval", new Position(0, 0), new Health(100, 100), 10, 5);
        messageCallback = new TestMessageCallback();
    }

    @Test
    void testInitialLevelAndExperience() {
        assertEquals(1, player.getLevel());
        assertEquals(0, player.getExperience());
    }

    @Test
    void testHandlePlayerWin_LevelsUpAndExperience() {
        player.handlePlayerWin(200, messageCallback);

        assertTrue(player.getLevel() > 1);

        String msg = messageCallback.getLastMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("reached level"));
        assertTrue(msg.contains("Health"));
        assertTrue(msg.contains("Attack"));
        assertTrue(msg.contains("Defense"));
    }

    @Test
    void testIsAliveAndSetDead() {
        assertTrue(player.isAlive());

        player.setDead();

        assertFalse(player.isAlive());
    }

    @Test
    void testGetTileAliveAndDead() {
        assertEquals('@', player.getTile());

        player.setDead();

        assertEquals('X', player.getTile());
    }

    @Test
    void testStringPlayerStats() {
        String stats = player.StringPlayerStats();

        assertTrue(stats.contains("yuval"));
        assertTrue(stats.contains("Health"));
        assertTrue(stats.contains("Attack"));
        assertTrue(stats.contains("Defense"));
        assertTrue(stats.contains("Level"));
        assertTrue(stats.contains("Experience"));
    }

    @Test
    void testPrintStats() {
        player.printStats(messageCallback);

        assertEquals(player.StringPlayerStats(), messageCallback.getLastMessage());
    }
}
