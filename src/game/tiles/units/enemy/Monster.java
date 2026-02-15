package game.tiles.units.enemy;

import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.TileVisitor;
import game.tiles.units.player.Player;

import java.util.Random;

public class Monster extends Enemy {

    private final int visionRange;

    public Monster(String name, char tile, Position position, Health health, int attackPoints, int defensePoints, int visionRange, int experienceValue) {
        super(name, tile, position, health, attackPoints, defensePoints, experienceValue);
        
        this.visionRange= visionRange;
    }

    @Override
    public Position onTurn(Player player) {
        var playerPosition = player.getPosition();

        // calculate the range to the player
        var range = this.position.range(playerPosition);
        if (range < this.visionRange) {
            var dx = this.position.getCol() - playerPosition.getCol(); // enemy to player dx
            var dy = this.position.getRow()- playerPosition.getRow();  // enemy to player dy

            if (Math.abs(dx) > Math.abs(dy)) {
                // the distance is greater on the x axis
                if (dx > 0) {
                    // the enemy is on the right hand of the player
                    return this.position.GetTargetDirection(Position.Direction.LEFT);
                } else {
                    return this.position.GetTargetDirection(Position.Direction.RIGHT);
                }
            } else {
                // the distance is bigger on the y axis
                if (dy>0) {
                    // enemy under the player
                    return this.position.GetTargetDirection(Position.Direction.UP);
                } else {
                    return this.position.GetTargetDirection(Position.Direction.DOWN);
                }
            }
        }

        return this.position.GetTargetDirection(Position.Direction.RANDOM);
    }

    @Override
    public String StringEnemyStats() {
        return super.StringEnemyStats() + "Vision Range: " +this.visionRange;
    }
}
