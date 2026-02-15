package tests;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;
import game.tiles.units.enemy.Boss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BossTest {

    private Boss boss;

    // Dummy MessageCallback to capture output or ignore
    private static class DummyCallback implements MessageCallback {
        @Override
        public void send(String message) {
            // For testing, we could print or collect messages here
            // System.out.println("Callback: " + message);
        }
    }

    // Dummy Player with constructor that accepts position and fixed defense
    static class DummyPlayer extends Player {
        public DummyPlayer(Position position) {
            super("Dummy", '@', position, new Health(100, 100), 10, 5);
        }

        @Override
        public int RollDefensePoints(MessageCallback cb) {
            return 3;  // fixed defense for testing
        }

        @Override
        public void castAbility(MessageCallback cb, ClearSightCallback clearSightCallback, List<Enemy> enemies, Player player) {

        }
    }

    @BeforeEach
    public void setup() {
        Position bossPos = new Position(5, 5);
        Health bossHealth = new Health(100, 100);
        // visionRange=5, experienceValue=50, abilityFrequency=3
        boss = new Boss("Boss", 'B', bossPos, bossHealth, 15, 10, 5, 50, 3);
    }

    @Test
    public void testOnTurn_randomWhenPlayerOutOfVisionRange() {
        Player playerFar = new DummyPlayer(new Position(20, 20)); // far away

        Position nextPos = boss.onTurn(playerFar);

        // Because player is out of visionRange, position should be random direction move
        // We expect a new position adjacent to boss.position (within 1 step)

        // Let's check that it's different but adjacent:
        int dRow = Math.abs(nextPos.getRow() - boss.getPosition().getRow());
        int dCol = Math.abs(nextPos.getCol() - boss.getPosition().getCol());

        assertTrue((dRow == 1 && dCol == 0) || (dRow == 0 && dCol == 1) || (dRow == 0 && dCol == 0),
                "Boss should move randomly or stay in place");
    }

    @Test
    public void testOnTurn_movesTowardsPlayerWhenInRangeAndAbilityNotReady() {
        // Place player within vision range, 2 columns to right
        Player playerRight = new DummyPlayer(new Position(5, 7));

        // First 3 calls increment combatTicks, on 3rd call ability activates instead of move.
        // For this test we expect a move, so reset combatTicks to 0
        // (combatTicks initially 0 on new Boss)

        Position nextPos = boss.onTurn(playerRight);

        assertEquals(boss.getPosition().GetTargetDirection(Position.Direction.RIGHT), nextPos);
    }



}
