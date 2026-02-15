package game.tiles.units.enemy;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.HeroicUnit;
import game.tiles.units.player.Player;
import view.CLI;

import java.util.List;
import java.util.Random;

public class Boss extends Enemy implements HeroicUnit {

    private final int visionRange;
    private final int abilityFrequency;
    private int combatTicks;

    public Boss(String name, char tile, Position position, Health health, int attackPoints, int defensePoints, int visionRange, int experienceValue, int abilityFrequency) {
        super(name, tile, position, health, attackPoints, defensePoints, experienceValue);

        this.visionRange= visionRange;
        this.abilityFrequency = abilityFrequency;
        this.combatTicks = 0;

    }


    @Override
    public Position onTurn(Player player) {
        var playerPosition = player.getPosition();
        var Range = this.position.range(playerPosition);


        if (Range < this.visionRange) {
            if (this.combatTicks == this.abilityFrequency) {
                this.combatTicks = 0;
                this.castAbility(CLI::DisplayMessage, null, null, player);
            }
            else {
                this.combatTicks++;
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
        }
        else {
            this.combatTicks = 0;
            return this.position.GetTargetDirection(Position.Direction.RANDOM);
        }
        return super.onTurn(player);
    }


    @Override
    public void castAbility(MessageCallback cb, ClearSightCallback cleanSightCallback, List<Enemy> enemies, Player player) {
        var hitAmount = this.attackPoints;
        cb.send(this.name +"Boss used Shoebodybop ability.");
        Random random = new Random();
        var defensePoints = player.RollDefensePoints(cb);
        cb.send(this.name +" Boss hit " +player.getName()+" for " + (hitAmount-defensePoints) +" ability damage.");

        player.getHealth().decreaseHealth(hitAmount);
    }
}
