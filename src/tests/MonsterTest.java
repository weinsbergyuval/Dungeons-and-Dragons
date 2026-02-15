package tests;

import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Monster;
import game.tiles.units.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {

    private Monster monster;
    private Position monsterPosition;

    // Concrete Player subclass for testing (to avoid abstract errors)
    static class TestPlayer extends Player {
        private final Position pos;

        public TestPlayer(Position pos) {
            super("yuval", '@', pos, new Health(100, 100), 10, 5);
            this.pos = pos;
        }


        @Override
        public void OnGameRound() {}

        @Override
        public void castAbility(game.callbacks.MessageCallback cb, game.callbacks.ClearSightCallback cleanSightCallback,
                                java.util.List<game.tiles.units.enemy.Enemy> enemies, Player player) {}

        @Override
        public String postLevelingUp(game.callbacks.MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
            return "";
        }

        @Override
        public Position getPosition() {
            return pos;
        }
    }

    @BeforeEach
    void setUp() {
        monsterPosition = new Position(5, 5);
        monster = new Monster("monster", 'O', monsterPosition, new Health(100, 100), 10, 5, 3, 50);
    }

    @Test
    void testStringEnemyStats_ContainsVisionRange() {
        String stats = monster.StringEnemyStats();
        assertTrue(stats.contains("Vision Range: 3"));
        assertTrue(stats.contains("monster"));
    }

    @Test
    void testOnTurn_PlayerOutOfVisionRange_ReturnsRandomDirection() {
        TestPlayer farPlayer = new TestPlayer(new Position(10, 10)); // range > visionRange=3
        Position move = monster.onTurn(farPlayer);
        assertNotNull(move);
        // Should NOT be equal to current position because it moves randomly
        assertNotEquals(monsterPosition, move);
    }

    @Test
    void testOnTurn_PlayerToLeft_MovesLeft() {
        TestPlayer player = new TestPlayer(new Position(5, 3));
        Position expected = monsterPosition.GetTargetDirection(Position.Direction.LEFT);
        assertEquals(expected, monster.onTurn(player));
    }

    @Test
    void testOnTurn_PlayerToRight_MovesRight() {
        TestPlayer player = new TestPlayer(new Position(5, 7));
        Position expected = monsterPosition.GetTargetDirection(Position.Direction.RIGHT);
        assertEquals(expected, monster.onTurn(player));
    }

    @Test
    void testOnTurn_PlayerAbove_MovesUp() {
        TestPlayer player = new TestPlayer(new Position(3, 5));
        Position expected = monsterPosition.GetTargetDirection(Position.Direction.UP);
        assertEquals(expected, monster.onTurn(player));
    }

    @Test
    void testOnTurn_PlayerBelow_MovesDown() {
        TestPlayer player = new TestPlayer(new Position(7, 5));
        Position expected = monsterPosition.GetTargetDirection(Position.Direction.DOWN);
        assertEquals(expected, monster.onTurn(player));
    }
}
