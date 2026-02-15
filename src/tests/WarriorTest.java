package tests;

import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Monster;
import game.tiles.units.player.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WarriorTest {

    private Warrior warrior;
    private StringBuilder output;

    @BeforeEach
    void setUp() {
        output = new StringBuilder();
        warrior = new Warrior("Gladiator", new Position(0, 0),
                new Health(100, 100), 20, 5, 3);
    }

    private MessageCallback mockCallback() {
        return msg -> output.append(msg).append("\n");
    }

    @Test
    void testInitialCooldownAndStats() {
        String stats = warrior.StringPlayerStats();
        assertTrue(stats.contains("CoolDown: 0"));
    }

    @Test
    void testCastFailsWhenOnCooldown() {
        warrior.OnGameRound(); // simulate success
        warrior.castAbility(mockCallback(), null, List.of(), warrior);
        warrior.OnGameRound(); // should set cooldown
        warrior.castAbility(mockCallback(), null, List.of(), warrior); // attempt to cast again too soon

        assertTrue(output.toString().contains("cooldown not over"));
    }

    @Test
    void testCastSucceedsAndHeals() {
        warrior.setDead(); // to check if `getTile()` switches
        warrior = new Warrior("Thor", new Position(0, 0), new Health(100, 50), 15, 10, 3); // 10 defense = 100 heal
        Monster dummy = new Monster("Goblin", 'A', new Position(0, 1), new Health(100, 100), 0,0,0, 2);

        warrior.castAbility(mockCallback(), null, List.of(dummy), warrior);

        assertTrue(output.toString().contains("used Avenger's Shield, healing for 100"));
        assertTrue(output.toString().contains("hit Goblin"));
        assertEquals(100, warrior.getHealth().getHealthAmount());
    }

    @Test
    void testCastWithNoEnemiesInRange() {
        Monster farEnemy = new Monster("Goblin", 'A', new Position(10, 10), new Health(100, 100), 0,0,0, 2);
        warrior.castAbility(mockCallback(), null, List.of(farEnemy), warrior);

        assertTrue(output.toString().contains("no enemy in range"));
    }

    @Test
    void testCooldownDecreasesEachRound() {
        warrior.castAbility(mockCallback(), null, List.of(), warrior); // sets cooldown to 3
        assertTrue(warrior.StringPlayerStats().contains("CoolDown: 0")); // cooldown is set at end of round
        warrior.OnGameRound(); // should set remainingCooldown
        warrior.OnGameRound(); // 2
        warrior.OnGameRound(); // 1
        warrior.OnGameRound(); // 0
        warrior.castAbility(mockCallback(), null, List.of(), warrior); // should work again
        assertTrue(output.toString().contains("used Avenger's Shield"));
    }

    @Test
    void testPostLevelingUpAppliesCorrectBonuses() {
        int baseHP = warrior.getHealth().getHealthPool();
        int baseAtk = warrior.getAttackPoints();
        int baseDef = warrior.getDefensePoints();

        String msg = warrior.postLevelingUp(mockCallback(), 20, 10, 5);
        assertTrue(msg.contains("reached level"));
        assertTrue(warrior.getHealth().getHealthPool() > baseHP);
        assertTrue(warrior.getAttackPoints() > baseAtk);
        assertTrue(warrior.getDefensePoints() > baseDef);
    }
}
