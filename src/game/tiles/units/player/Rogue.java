package game.tiles.units.player;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Rogue extends Player {
    private final int maxEnergy = 100;
    private int currentEnergy;
    private int specialAbilityCost;
    private boolean castSucceeded;

    public Rogue(String name, Position position, Health health, int attackPoints, int defensePoints, int specialAbilityCost) {
        super(name, '@', position, health, attackPoints, defensePoints);
        this.currentEnergy = this.maxEnergy;
        this.specialAbilityCost = specialAbilityCost;
    }

    @Override
    public String StringPlayerStats() {
        return super.StringPlayerStats() + "Energy: " + this.currentEnergy + "/" + this.maxEnergy;
    }

    @Override
    public String postLevelingUp(MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
        this.currentEnergy = 100;

        int additionalAttack = this.attackPoints + (3 * level);
        int deltaAttack2 = additionalAttack - this.attackPoints;
        this.attackPoints = additionalAttack;

        String msg = this.name + " reached level " + this.level + ": +"
                + deltaHealth + " Health, +" + (deltaAttack + deltaAttack2) + " Attack, +"
                + deltaDefense + " Defense";

        return msg;
    }

    @Override
    public void OnGameRound() {
        if (castSucceeded) {
            castSucceeded = false;
            return;
        }
        this.currentEnergy = Math.min(this.currentEnergy + 10, this.maxEnergy);
    }

    private final int enemyRange = 2;

    @Override
    public void castAbility(MessageCallback cb, ClearSightCallback cleanSightCallback, List<Enemy> enemies, Player player) {
        if (this.currentEnergy < this.specialAbilityCost) {
            castSucceeded = false;
            cb.send(this.name + " does not have enough energy to cast special ability.");
        } else {
            castSucceeded = true;
            this.currentEnergy = this.currentEnergy - this.specialAbilityCost;

            // get a list of potential enemies in range
            List<Enemy> potentialEnemies = new ArrayList<>();
            enemies.forEach(e -> {
                var range = this.position.range(e.getPosition());
                if (range < enemyRange) {
                    potentialEnemies.add(e);
                }
            });
            //NOTE: I thought it would be nice to see how much enemies around the mage
            cb.send(this.name + " cast Fan of Knives (" +  potentialEnemies.size() + " enemies in range)");

            if (potentialEnemies.isEmpty()) {
                castSucceeded = false;
                cb.send("no enemy in range.. cast didn't affect any enemy...");
            } else {
                castSucceeded = true;
                var hitAmount = this.attackPoints;
                for (int i = 0; i < potentialEnemies.size(); i++) {
                    var targetEnemy = potentialEnemies.get(i);
                    var defensePoints = targetEnemy.RollDefensePoints(cb);
                    cb.send(this.name + " hit " + targetEnemy.getName() + " for " + (hitAmount - defensePoints) + " ability damage.");
                    targetEnemy.getHealth().decreaseHealth(hitAmount);

                }


            }
        }
    }
}
