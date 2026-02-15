package game.tiles.units.player;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hunter extends Player{
    private int shootingRange;
    private int arrowCount;
    private int ticksCount;
    private boolean castSucceeded;



    public Hunter(String name, Position position, Health health, int attackPoints, int defensePoints, int shootingRange) {
        super(name, '@', position, health, attackPoints, defensePoints);
        this.shootingRange = shootingRange;
        this.arrowCount = 10*this.level;
        this.ticksCount = 0;
    }

    @Override
    public String postLevelingUp(MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
        int addArrows = this.arrowCount + (10*this.level); //according to instructions but needs to be "nerfed" - to powerful !!
        int deltaArrows = addArrows - this.arrowCount;
        this.arrowCount = addArrows;

        int additionalAttack = this.attackPoints + (2 * this.level);
        int deltaAttack2 = additionalAttack - this.attackPoints;
        this.attackPoints = additionalAttack;

        int additionalDefense = this.defensePoints + (this.level);
        int deltaDefense2 = additionalDefense - this.defensePoints;
        this.defensePoints = additionalDefense;

        String msg = this.name + " reached level " + this.level + ": +"
                + deltaHealth + " Health, +" + (deltaAttack + deltaAttack2) + " Attack, +"
                + (deltaDefense + deltaDefense2) + " Defense, +" + deltaArrows +" Arrows";
                //also nice to know how much arrow player gained

        return msg;
    }

    @Override
    public void OnGameRound() {
        if (castSucceeded) {
            castSucceeded = false;
            this.ticksCount++; //instructions were not clear about this here
            return;
        }
        if (this.ticksCount == 10){
            this.arrowCount = this.arrowCount + this.level;
            this.ticksCount = 0;
        }
        else {
            this.ticksCount++;
        }
    }

    @Override
    public String StringPlayerStats() {
        return super.StringPlayerStats() + "Arrows: " + this.arrowCount + "                " + "Range: " + this.shootingRange;
    }


    @Override
    public void castAbility(MessageCallback cb, ClearSightCallback cleanSightCallback, List<Enemy> enemies, Player player) {
        if (this.arrowCount == 0) {
            castSucceeded = false;
            cb.send(this.name + " has 0 arrows. cannot cast special ability.");
            return;
        } else {
            // get a list of potential enemies in range
            List<Enemy> potentialEnemies = new ArrayList<>();
            enemies.forEach(e -> {
                var Range = this.position.range(e.getPosition());
                if (Range < this.shootingRange) {
                    potentialEnemies.add(e);
                }
            });
            if (potentialEnemies.isEmpty()){
                castSucceeded = false;
                cb.send("No enemies in shooting range.. cannot cast special ability.");
                return;
            }
            castSucceeded = true;
            this.arrowCount--;
            Enemy closestEnemy = potentialEnemies.stream()
                    .min(Comparator.comparingDouble(enemy -> this.position.range(enemy.getPosition())))
                    .orElse(null);


            boolean enemyInClearSight = cleanSightCallback.hasClearSight(this.getPosition(), closestEnemy.getPosition());
            if (enemyInClearSight) {
                cb.send(this.name + " fired an arrow at " + closestEnemy.getName() + ".");
                var defensePoints = closestEnemy.RollDefensePoints(cb);
                int hitAmount = this.attackPoints - defensePoints;
                cb.send(this.name + " hit " + closestEnemy.getName() + " for " + hitAmount + " ability damage.");
                closestEnemy.getHealth().decreaseHealth(hitAmount);
            } else {
                cb.send(this.name + " can not fire an arrow at " + closestEnemy.getName() + " - no clear sight.");
            }
            }

        }

    }

