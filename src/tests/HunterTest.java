package tests;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Hunter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HunterTest {

    private Hunter hunter;
    private TestMessageCallback messageCallback;
    private Position position;

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

    static class TestClearSightCallback implements ClearSightCallback {
        private final boolean hasSight;

        public TestClearSightCallback(boolean hasSight) {
            this.hasSight = hasSight;
        }

        @Override
        public boolean hasClearSight(Position from, Position to) {
            return hasSight;
        }
    }

    static class DummyEnemy extends Enemy {
        private final Position pos;
        private final String enemyName;
        private Health health;
        private int defensePoints;

        public DummyEnemy(String name, Position position, int healthAmount, int defensePoints) {
            super(name, 'E', position, new Health(healthAmount, healthAmount), 0, defensePoints, 10);
            this.pos = position;
            this.enemyName = name;
            this.health = getHealth();
            this.defensePoints = defensePoints;
        }

        @Override
        public Position getPosition() {
            return pos;
        }

        @Override
        public String getName() {
            return enemyName;
        }

        @Override
        public int RollDefensePoints(MessageCallback cb) {
            return defensePoints;
        }

        @Override
        public Health getHealth() {
            return health;
        }

        @Override
        public void accept(game.tiles.units.TileVisitor visitor) {
            // no-op
        }
    }

    @BeforeEach
    void setUp() {
        position = new Position(0, 0);
        hunter = new Hunter("ygritte", position, new Health(100, 100), 20, 10, 5);
        messageCallback = new TestMessageCallback();
    }

    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    void testOnGameRound_CastSucceededResetsFlagAndIncrementsTicks() throws Exception {
        setPrivateField(hunter, "castSucceeded", true);
        setPrivateField(hunter, "ticksCount", 5);

        hunter.OnGameRound();

        boolean castSucceededAfter = (boolean) getPrivateField(hunter, "castSucceeded");
        int ticksCountAfter = (int) getPrivateField(hunter, "ticksCount");

        assertFalse(castSucceededAfter);
        assertEquals(6, ticksCountAfter);
    }

    @Test
    void testCastAbility_NoArrows() throws Exception {
        setPrivateField(hunter, "arrowCount", 0);

        hunter.castAbility(messageCallback, new TestClearSightCallback(true), new ArrayList<>(), hunter);

        boolean castSucceeded = (boolean) getPrivateField(hunter, "castSucceeded");
        assertFalse(castSucceeded);
        assertEquals("ygritte has 0 arrows. cannot cast special ability.", messageCallback.getLastMessage());
    }

    @Test
    void testCastAbility_NoEnemiesInRange() throws Exception {
        List<Enemy> enemies = new ArrayList<>();
        setPrivateField(hunter, "arrowCount", 5);

        hunter.castAbility(messageCallback, new TestClearSightCallback(true), enemies, hunter);

        boolean castSucceeded = (boolean) getPrivateField(hunter, "castSucceeded");
        assertFalse(castSucceeded);
        assertEquals("No enemies in shooting range.. cannot cast special ability.", messageCallback.getLastMessage());
    }

    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    // You can add more tests similarly by reading and setting private fields via reflection.

}
