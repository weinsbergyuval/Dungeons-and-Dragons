package tests;

import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.player.Player;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    private Enemy enemy;
    private Health health;
    private Position position;

    static class TestEnemy extends Enemy {
        public TestEnemy(String name, char tile, Position position, Health health, int attackPoints, int defensePoints, int experienceValue) {
            super(name, tile, position, health, attackPoints, defensePoints, experienceValue);
        }
    }

    @BeforeEach
    void setUp() {
        position = new Position(0, 0);
        health = new Health(100, 100);
        enemy = new TestEnemy("enemy", 'G', position, health, 10, 5, 50);
    }

    @Test
    void testGetExperienceValue() {
        assertEquals(50, enemy.getExperienceValue());
    }

    @Test
    void testIsDead_False() {
        assertFalse(enemy.isDead());
    }

    @Test
    void testIsDead_True() {
        health.decreaseHealth(health.getHealthAmount()); // set health to 0
        assertTrue(enemy.isDead());
    }

    @Test
    void testStringEnemyStats_ContainsNameAndStats() {
        String stats = enemy.StringEnemyStats();
        assertTrue(stats.contains("enemy"));
        assertTrue(stats.contains("Health: 100/100"));
        assertTrue(stats.contains("Attack: 10"));
        assertTrue(stats.contains("Defense: 5"));
        assertTrue(stats.contains("Experience Value: 50"));
    }

    @Test
    void testPrintStats_SendsMessage() {
        MessageCallback callback = message -> assertTrue(message.contains("enemy"));
        enemy.printStats(callback);
    }
}
