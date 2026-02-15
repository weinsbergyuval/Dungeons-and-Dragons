package game.tiles.units.enemy;

import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.TileVisitor;
import game.tiles.units.Unit;
import game.tiles.units.player.Player;

public abstract class Enemy extends Unit {

    private final int experienceValue;

    public Enemy(String name, char tile, Position position, Health health, int attackPoints, int defensePoints, int experienceValue) {
        super(name, tile, position, health, attackPoints, defensePoints);
        this.experienceValue = experienceValue;
    }

    public int getExperienceValue(){
        return this.experienceValue;
    }

    // onTurn is called after the player is moved, in order to let the enemy perform some
    // logic, like following the player or move to another tile.
    // This method should return the target position of the enemy and if there is no
    // change the current position.
    public Position onTurn(Player player) {
        var playerPosition = player.getPosition();
        // default is to no move
        return this.position;
    }

    public boolean isDead() {
        return (getHealth().getHealthAmount() <= 0);
    }

    @Override
    public void accept(TileVisitor visitor) {
        visitor.visit(this);
    }


    public String StringEnemyStats(){
        String spaces = "                ";
        String EnemyStats = this.name +spaces+ "Health: " +
                this.health.getHealthAmount() +"/" +this.health.getHealthPool() +
                spaces+ "Attack: "+ this.attackPoints +
                spaces+ "Defense: " +this.defensePoints +
                spaces+ "Experience Value: " +this.experienceValue + spaces;
        return EnemyStats;
    }

    @Override
    public void printStats(MessageCallback callback) {
        callback.send(this.StringEnemyStats());
    }
}
