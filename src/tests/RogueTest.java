package tests;

import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Monster;
import game.tiles.units.player.Rogue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RogueTest {

    private Rogue rogue;
    private StringBuilder output;
    private MessageCallback cb;

    @BeforeEach
    void setUp() {
        output = new StringBuilder();
        cb = msg -> output.append(msg).append("\n");

        rogue = new Rogue("yuval", new Position(0, 0), new Health(100, 100), 20, 10, 30);
    }

    private Monster createEnemyAt(int row, int col) {
        return new Monster("monster", 'A', new Position(row, col),
                new Health(100, 100), 0, 0, 0, 2);
    }

    private void exhaustEnergy() {
        Monster dummy = createEnemyAt(1, 0);
        while (true) {
            output.setLength(0); // clear output
            rogue.castAbility(cb,null, List.of(dummy), rogue);
            if (output.toString().contains("not have enough energy")) {
                break;
            }
        }
    }


    @Test
    void testInitialEnergy() {
        String stats = rogue.StringPlayerStats();
        assertTrue(stats.contains("Energy: 100/100"));
    }

    @Test
    void testCastFailsWithoutEnoughEnergy() {
        exhaustEnergy(); // brings energy < 30
        output.setLength(0);
        rogue.castAbility(cb, null, List.of(), rogue); // try cast again
        assertTrue(output.toString().contains("does not have enough energy"));
    }

    @Test
    void testCastHitsEnemiesInRangeOnly() {
        Monster inRange1 = createEnemyAt(1, 0);  // distance = 1
        Monster inRange2 = createEnemyAt(1, 0);  // distance = 1
        Monster outOfRange = createEnemyAt(5, 5); // distance > 2

        rogue.castAbility(cb, null, List.of(inRange1, inRange2, outOfRange), rogue);

        String out = output.toString();
        assertTrue(out.contains("cast Fan of Knives"));
        assertTrue(out.contains("hit monster for"));
        assertEquals(2, out.lines().filter(line -> line.contains("hit monster for")).count());
    }

    @Test
    void testCastWithNoEnemiesInRange() {
        Monster farEnemy = createEnemyAt(10, 10);
        rogue.castAbility(cb, null, List.of(farEnemy), rogue);
        String out = output.toString();

        assertTrue(out.contains("no enemy in range"));
    }


    @Test
    void testNoRegenImmediatelyAfterCast() {
        Monster dummy = createEnemyAt(1, 0);
        rogue.castAbility(cb, null, List.of(dummy), rogue); // cast successful

        // No regen on this round
        rogue.OnGameRound();
        String stats = rogue.StringPlayerStats();
        assertFalse(stats.contains("Energy: 100/100")); // it won’t regen immediately
    }

    @Test
    void testLevelUpRestoresEnergyAndBuffsAttack() {
        String beforeStats = rogue.StringPlayerStats();
        String levelUpMessage = rogue.postLevelingUp(cb, 10, 5, 5);

        assertTrue(levelUpMessage.contains("reached level"));
        String afterStats = rogue.StringPlayerStats();

        assertTrue(afterStats.contains("Energy: 100/100"));
        assertNotEquals(beforeStats, afterStats);
    }

    @Test
    void testStatsContainsEnergyInfo() {
        String stats = rogue.StringPlayerStats();
        assertTrue(stats.contains("Energy:"));
        assertTrue(stats.contains("/100"));
    }
}
