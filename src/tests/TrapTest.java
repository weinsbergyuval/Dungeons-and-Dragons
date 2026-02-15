package tests;

import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Trap;
import game.tiles.units.player.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrapTest {

    private Trap trap;

    @BeforeEach
    void setUp() {
        trap = new Trap("Test Trap", 'T', new Position(0, 0),
                new Health(100, 100), 30, 5, 25, 2, 3);
    }

    @Test
    void testInitialVisibility() {
        assertEquals('T', trap.getTile());
    }

    @Test
    void testVisibilityToInvisibility() {
        Warrior warrior = new Warrior("Gladiator", new Position(5, 5),
                new Health(100, 100), 20, 5, 3);
        trap.onTurn(warrior); // tick 1
        trap.onTurn(warrior); // tick 2: should now be invisible
        assertEquals('.', trap.getTile());
    }

    @Test
    void testVisibilityToVisibility() {
        Warrior warrior = new Warrior("Gladiator", new Position(5, 5),
                new Health(100, 100), 20, 5, 3);
        trap.onTurn(warrior); // tick 1
        trap.onTurn(warrior); // tick 2 - invisible
        trap.onTurn(warrior); // tick 1 - invisible
        trap.onTurn(warrior); // tick 2 - invisible
        trap.onTurn(warrior); // tick 3 - visible
        assertEquals('T', trap.getTile());
    }


    @Test
    void testOnTurnReturnsSamePositionWhenPlayerOutOfRange() {
        Warrior playerFar = new Warrior("Gladiator", new Position(5,5),
                new Health(100, 100), 20, 5, 3);
        Position result = trap.onTurn(playerFar);// range > 2
        assertEquals(trap.getPosition(), result);
    }

    @Test
    void testStringEnemyStatsIncludesVisibilityInfo() {
        String stats = trap.StringEnemyStats();
        assertTrue(stats.contains("Visibilty Time: 2"));
        assertTrue(stats.contains("Invisibilty Time: 3"));
    }
    @Test
    void testZeroVisibilityTime() {
        Trap instantInvisibleTrap = new Trap("Test Trap 2", 'G', new Position(0, 0),
                new Health(100, 100), 20, 5, 10, 0, 2);

        Warrior player = new Warrior("Gladiator", new Position(5,5),
                new Health(100, 100), 20, 5, 3);

        instantInvisibleTrap.onTurn(player);
        assertEquals('.', instantInvisibleTrap.getTile()); // Should instantly be invisible
    }

    @Test
    void testZeroInvisibilityTime() {
        Trap flickerTrap = new Trap("Test Trap 3", 'F', new Position(0, 0),
                new Health(100, 100), 20, 5, 10, 2, 0);

        Warrior player = new Warrior("Gladiator", new Position(5,5),
                new Health(100, 100), 20, 5, 3);

        // Go to invisible after 2 ticks
        flickerTrap.onTurn(player);
        flickerTrap.onTurn(player);
        assertEquals('.', flickerTrap.getTile());

        // Now after 0 ticks, should flip back to visible
        flickerTrap.onTurn(player);
        assertEquals('F', flickerTrap.getTile());
    }

    @Test
    void testTrapNeverBecomesInvisibleWithLargeVisibilityTime() {
        Warrior player = new Warrior("Gladiator", new Position(5,5),
                new Health(100, 100), 20, 5, 3);

        Trap alwaysVisibleTrap = new Trap("Not Invisible Trap", 'I', new Position(0, 0),
                new Health(100, 100), 10, 2, 5, 1000, 3); // huge visibilityTime

        for (int i = 0; i < 10; i++) {
            alwaysVisibleTrap.onTurn(player);
        }
        assertEquals('I', alwaysVisibleTrap.getTile()); // Still visible
    }

}
