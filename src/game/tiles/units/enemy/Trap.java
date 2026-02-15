package game.tiles.units.enemy;

import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.TileVisitor;
import game.tiles.units.player.Player;

public class Trap extends Enemy{

    private final int visibiltyTime;
    private final int invisibiltyTime;
    private int ticksCount;
    private boolean visible;

    public Trap(String name, char tile, Position position, Health health, int attackPoints, int defensePoints, int experienceValue, int visibiltyTime, int invisibiltyTime) {
        super(name, tile, position, health, attackPoints, defensePoints, experienceValue);

        this.visibiltyTime= visibiltyTime;
        this.invisibiltyTime = invisibiltyTime;
        this.ticksCount = 0;
        this.visible = true;
    }

    @Override
    public String StringEnemyStats() {
        return super.StringEnemyStats() + "Visibilty Time: " +this.visibiltyTime+ "                "
                + "Invisibilty Time: " +this.invisibiltyTime;
    }

    @Override
    public Position onTurn(Player player) {
        Position playerPosition = player.getPosition() ;
        // calculate the visibility status
        this.ticksCount++;

        if (this.visible && !(this.ticksCount < this.visibiltyTime)) {
            // we need to flip change
            flipVisibility();
        } else if (!this.visible && !(this.ticksCount < this.invisibiltyTime)) {
            // we need to flip visibility
            flipVisibility();
        }

        // check if we need to attack the player, if so, we
        // want to return the position of the player that means that
        // we want to "move" to the player tile.
        if (this.position.range(playerPosition) < 2) {
            return playerPosition;
        }

        // no change in the position of the trap
        return super.onTurn(player);
    }

    private void flipVisibility() {
        this.visible = !this.visible;
        this.ticksCount = 0;
    }

    @Override
    public char getTile() {
        if (this.visible) {
            return super.getTile();
        } else {
            return '.';
        }
    }
}


