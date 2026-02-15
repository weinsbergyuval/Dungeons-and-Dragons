package game.tiles.units.player;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Warrior extends Player{
    private int abilityCooldown;
    private int remainingCooldown;
    private boolean castSucceeded ;

    public Warrior (String name, Position position, Health health, int attackPoints, int defensePoints, int cooldown){
        super(name, '@', position, health, attackPoints, defensePoints);
        this.abilityCooldown = cooldown;
        this.remainingCooldown = 0;
        this.castSucceeded = false;
    }
    @Override
    public String StringPlayerStats() { //todo make in all extending classes with super
        return super.StringPlayerStats() + "CoolDown: " + this.remainingCooldown;
    }


    @Override
    public String postLevelingUp(MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
        this.remainingCooldown = 0;
        int additionalHealthPool = this.health.getHealthPool() + (5*this.level);
        int deltaHealth2 = additionalHealthPool - this.health.getHealthPool();
        this.health.setHealthPool(additionalHealthPool);
        this.health.setHealthAmount(this.health.getHealthPool());


        int additionalAttack = this.attackPoints + (2*level);
        int deltaAttack2 = additionalAttack - this.attackPoints;
        this.attackPoints = additionalAttack;

        int additionalDefense = this.defensePoints + (this.level);
        int deltaDefense2 = additionalDefense - this.defensePoints;
        this.defensePoints = additionalDefense;

        String msg = this.name + " reached level " + this.level + ": +"
                + (deltaHealth+deltaHealth2) + " Health, +" + (deltaAttack+deltaAttack2) + " Attack, +"
                + (deltaDefense+deltaDefense2) + " Defense";

        return msg;

    }

    @Override
    public void OnGameRound() {
        if (castSucceeded) {
            this.remainingCooldown = this.abilityCooldown;
            castSucceeded = false;
            return;
        }

        this.remainingCooldown = Math.max(0, this.remainingCooldown - 1);
    }

    private final int castRange = 3;

    @Override
    public void castAbility(MessageCallback cb, ClearSightCallback cleanSightCallback, List<Enemy> enemies, Player player){
        //todo needs to change everything to player.get or not
        if (this.remainingCooldown > 0){
            castSucceeded = false;
            cb.send("cooldown not over, " +this.name+ " cannot cast special ability.");
        } else {
            castSucceeded = true;

            // if we use the cast, reset the remainingCooldown
            var healAmount = 10*this.defensePoints;
            this.health.addHealth(healAmount);
            cb.send(this.name +" used Avenger's Shield, healing for " + healAmount + ".");

            // get a list of potential enemies in range
            List<Enemy> potentialEnemies = new ArrayList<>();
            enemies.forEach(e-> {
                var range = this.position.range(e.getPosition());
                if (range < castRange) {
                    potentialEnemies.add(e);
                }
            });

            if (potentialEnemies.isEmpty()) {
                cb.send("no enemy in range.. cast didn't affect any enemy..." );
            } else {
                var hitAmount = this.health.getHealthPool()/10;

                Random random = new Random();
                int index = random.nextInt(potentialEnemies.size());
                var targetEnemy = potentialEnemies.get(index);

                var defensePoints = targetEnemy.RollDefensePoints(cb);
                cb.send(this.name +" hit " +targetEnemy.getName()+" for " + (hitAmount-defensePoints) +" ability damage.");

                targetEnemy.getHealth().decreaseHealth(hitAmount);
            }
        }
    }



}
