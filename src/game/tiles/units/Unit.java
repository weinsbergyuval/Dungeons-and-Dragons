package game.tiles.units;

import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_components.Position;

import java.util.Random;

public abstract class Unit extends Tile {
    protected String name;
    protected Health health;
    protected int attackPoints;
    protected int defensePoints;

    public Unit(String name, char tile, Position position, Health health, int attackPoints, int defensePoints)  {
        super(tile, position);

        this.name = name;
        this.health = health;
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    public int getAttackPoints(){
        return this.attackPoints;
    }

    public int getDefensePoints(){
        return this.defensePoints;
    }

    public int RollAttackPoints(MessageCallback cb) {
        Random random = new Random();
        var attackRoll =  random.nextInt(this.getAttackPoints() + 1);
        if (cb!=null) {
            cb.send(getName() + " rolled " + attackRoll + " attack points.");
        }
        return attackRoll;
    }

    public int RollDefensePoints(MessageCallback cb) {
        Random random = new Random();
        var defenseRoll =  random.nextInt(this.getDefensePoints() + 1);
        if (cb!=null) {
            cb.send(getName() + " rolled " + defenseRoll + " defense points.");
        }
        return defenseRoll;
    }

    public String getName() {
        return this.name;
    }

    public Health getHealth() {
        return this.health;
    }

    public abstract void printStats(MessageCallback callback);
}
