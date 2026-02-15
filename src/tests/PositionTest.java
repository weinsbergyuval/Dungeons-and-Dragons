package tests;

import game.tiles.board_components.Position;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private static final long TEST_TIMEOUT_SECONDS = 5;

    @org.junit.jupiter.api.Test
    void getTargetDirection() {
        var pos = new Position(5,10);
        assertEquals(10, pos.getCol());
        assertEquals(5, pos.getRow());

        boolean allDirectionsTested = false;

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (TEST_TIMEOUT_SECONDS * 1000); // Calculate when to stop the loop

        Map<Position.Direction, Boolean> hits = new HashMap<>(5);

        while (System.currentTimeMillis() < endTime) {
            pos = new Position(10, 10);
            var rand = pos.GetTargetDirection(Position.Direction.RANDOM);
            if (rand.getCol() > pos.getCol()) {
                hits.put(Position.Direction.RIGHT, true);
            } else if (rand.getCol() < pos.getCol()) {
                hits.put(Position.Direction.LEFT, true);
            } else if (rand.getRow() > pos.getRow()) {
                hits.put(Position.Direction.DOWN, true);
            } else if (rand.getRow() < pos.getRow()) {
                hits.put(Position.Direction.UP, true);
            } else if (rand.getRow() == pos.getRow() && rand.getCol() == pos.getCol()) {
                hits.put(Position.Direction.NONE, true);
            }


            if (hits.containsKey(Position.Direction.RIGHT) &&
                    hits.containsKey(Position.Direction.LEFT) &&
                    hits.containsKey(Position.Direction.UP) &&
                    hits.containsKey(Position.Direction.DOWN) &&
                    hits.containsKey(Position.Direction.NONE)) {

                allDirectionsTested = true;
                break;
            }
        }

        System.out.println(hits);

        assertEquals(true, allDirectionsTested);
    }
}