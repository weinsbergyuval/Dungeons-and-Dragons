package tests;

import game.tiles.units.Health;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthTest {

    @Test
    public void testAddHealth_doesNotExceedHealthPool() {
        Health health = new Health(100, 50);
        health.addHealth(30);
        assertEquals(80, health.getHealthAmount());

        // Adding more than pool should cap at healthPool
        health.addHealth(50);
        assertEquals(100, health.getHealthAmount());
    }

    @Test
    public void testDecreaseHealth_doesNotGoBelowZero() {
        Health health = new Health(100, 50);
        health.decreaseHealth(20);
        assertEquals(30, health.getHealthAmount());

        // Decreasing more than current amount should floor at zero
        health.decreaseHealth(50);
        assertEquals(0, health.getHealthAmount());
    }

    @Test
    public void testSettersAndGettersWorkCorrectly() {
        Health health = new Health(100, 50);

        health.setHealthPool(120);
        assertEquals(120, health.getHealthPool());

        health.setHealthAmount(80);
        assertEquals(80, health.getHealthAmount());
    }

    @Test
    public void testAddHealth_whenHealthIsAtMax() {
        Health health = new Health(100, 100);
        health.addHealth(10);
        assertEquals(100, health.getHealthAmount());  // Should stay at max
    }

    @Test
    public void testDecreaseHealth_whenHealthIsZero() {
        Health health = new Health(100, 0);
        health.decreaseHealth(10);
        assertEquals(0, health.getHealthAmount()); // Should stay at zero
    }
}
