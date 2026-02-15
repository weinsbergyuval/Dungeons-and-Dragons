package tests;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Mage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MageTest {

    private Mage mage;
    private TestMessageCallback messageCallback;
    private TestClearSightCallback clearSightCallback;

    static class DummyEnemy extends Enemy {
        public DummyEnemy(String name, Position position) {
            super(name, 'E', position, new Health(100, 100), 5, 2, 20);
        }

        @Override
        public int RollDefensePoints(MessageCallback cb) {
            return 2; // fixed for test predictability
        }
    }

    static class TestMessageCallback implements MessageCallback {
        public final List<String> messages = new ArrayList<>();

        @Override
        public void send(String message) {
            messages.add(message);
        }
    }

    static class TestClearSightCallback implements ClearSightCallback {
        @Override
        public boolean hasClearSight(Position from, Position to) {
            return true; // always clear sight in tests
        }
    }

    @BeforeEach
    void setUp() {
        mage = new Mage("mage", new Position(0, 0), new Health(100, 100), 10, 5,
                100, 25, 30, 3, 5);
        messageCallback = new TestMessageCallback();
        clearSightCallback = new TestClearSightCallback();
    }

    @Test
    void testConstructorSetsInitialManaCorrectly() {
        assertEquals(25, mage.getCurrentMana()); // manaPool / 4
    }

    @Test
    void testOnGameRound_IncreasesMana() {
        mage.OnGameRound();
        assertEquals(26, mage.getCurrentMana()); // level is 1 by default
    }

    @Test
    void testOnGameRound_WhenCastSucceeded_PreventsManaRegen() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new DummyEnemy("enemy1", new Position(1, 1)));
        mage.castAbility(messageCallback, clearSightCallback, enemies, mage); // sets castSucceeded = true

        int manaBefore = mage.getCurrentMana();
        mage.OnGameRound(); // should skip regen due to castSucceeded
        assertEquals(manaBefore, mage.getCurrentMana());
    }


    @Test
    void testCastAbility_NotEnoughMana() {
        mage = new Mage("mage", new Position(0, 0), new Health(100, 100), 10, 5,
                100, 50, 20, 2, 5); // manaCost = 50 > currentMana = 25
        mage.castAbility(messageCallback, clearSightCallback, new ArrayList<>(), mage);

        assertTrue(messageCallback.messages.get(0).contains("does not have enough mana"));
    }

    @Test
    void testCastAbility_HitsEnemiesInSight() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new DummyEnemy("enemy1", new Position(1, 1)));
        enemies.add(new DummyEnemy("enemy2", new Position(2, 2)));

        mage.castAbility(messageCallback, clearSightCallback, enemies, mage);

        assertTrue(messageCallback.messages.stream().anyMatch(msg -> msg.contains("cast blizzard")));
        assertTrue(messageCallback.messages.stream().anyMatch(msg -> msg.contains("hit")));
    }

    @Test
    void testCastAbility_NoEnemiesInRange() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new DummyEnemy("FarEnemy", new Position(100, 100))); // too far

        mage.castAbility(messageCallback, clearSightCallback, enemies, mage);

        assertTrue(messageCallback.messages.stream().anyMatch(msg -> msg.contains("no enemy in range")));
    }
}
