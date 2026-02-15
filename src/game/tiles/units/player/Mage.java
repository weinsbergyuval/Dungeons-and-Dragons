package game.tiles.units.player;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mage extends Player {
    private int manaPool;
    private int manaCost;
    private int spellPower;
    private int currentMana;
    private int hitsCount;
    private int abilityRange;
    private boolean castSucceeded;


    public Mage(String name, Position position, Health health, int attackPoints, int defensePoints, int manaPool, int manaCost, int spellPower, int hitsCount, int abilityRange) {
        super(name, '@', position, health, attackPoints, defensePoints);
        this.manaPool = manaPool;
        this.spellPower = spellPower;
        this.manaCost = manaCost;
        this.currentMana = manaPool / 4;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    @Override
    public String StringPlayerStats() {
        return super.StringPlayerStats() + "Mana: " + this.currentMana + "/" + this.manaPool + "                " + "Spell Power: " + this.spellPower;
    }

    @Override
    public String postLevelingUp(MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
        int addMana = this.manaPool + (25 * this.level);
        int deltaMana = addMana - this.manaPool;
        this.manaPool = addMana;
        this.currentMana = Math.min(this.currentMana + (this.manaPool / 4), this.manaPool);

        int addSpellPower = this.spellPower + (10 * this.level);
        int deltaSpellPower = addSpellPower - this.spellPower;
        this.spellPower = addSpellPower;

        String msg = this.name + " reached level " + this.level + ": +"
                + deltaHealth + " Health, +" + deltaAttack + " Attack, +"
                + deltaDefense + " Defense, +" + deltaMana + " maximum mana, +" + deltaSpellPower + " spell power";

        return msg;
    }

    @Override
    public void OnGameRound() {
        if (castSucceeded) {
            castSucceeded = false;
            return;
        }
        this.currentMana = Math.min(this.manaPool, this.currentMana + this.level);
    }


    @Override
    public void castAbility(MessageCallback cb, ClearSightCallback cleanSightCallback, List<Enemy> enemies, Player player) {
        if (this.currentMana < this.manaCost) {
            castSucceeded = false;
            cb.send(this.name + " does not have enough mana to cast special ability.");
        } else {
            //cb.send(this.name +" cast blizzard.");
            castSucceeded = true;
            this.currentMana = this.currentMana - this.manaCost;

            // get a list of potential enemies in range
            List<Enemy> potentialEnemies = new ArrayList<>();
            enemies.forEach(e -> {
                var range = this.position.range(e.getPosition());
                if (range < this.abilityRange) {
                    boolean enemyInClearSight = cleanSightCallback.hasClearSight(this.getPosition(), e.getPosition());
                    if (enemyInClearSight) {
                        potentialEnemies.add(e);
                    }
                }
            });

            //NOTE: I thought it would be nice to see how much enemies around the mage
            cb.send(this.name +" cast blizzard (" +  potentialEnemies.size() + " enemies in sight)");

            int hits = 0;
            while (hits < this.hitsCount) {
                if (potentialEnemies.isEmpty()) {
                    castSucceeded = false;
                    cb.send("no enemy in range or no clear sight.. cast didn't affect any enemy...");
                    break;
                } else {

                    castSucceeded = true;
                    var hitAmount = this.spellPower;
                    hits++;

                    Random random = new Random();
                    int index = random.nextInt(potentialEnemies.size());
                    var targetEnemy = potentialEnemies.get(index);


                    var defensePoints = targetEnemy.RollDefensePoints(cb);
                    cb.send(this.name + " hit " + targetEnemy.getName() + " for " + (hitAmount - defensePoints) + " ability damage.");

                    targetEnemy.getHealth().decreaseHealth(hitAmount);
                }

            }

        }
    }

    //for tests

    public int getCurrentMana() {
        return currentMana;
    }

    public int getManaPool() {
        return manaPool;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getSpellPower() {
        return spellPower;
    }
}
