package game.tiles.units.player;

import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.HeroicUnit;
import game.tiles.units.TileVisitor;
import game.tiles.units.Unit;

public abstract class Player extends Unit implements HeroicUnit {

    protected int level;
    protected int experience;
    private boolean isAlive;

    protected Player(String name, char tile, Position position, Health health, int attackPoints, int defensePoints) {
        super(name, tile, position, health, attackPoints, defensePoints);
        this.level = 1;
        this.experience = 0;
        this.isAlive = true;
    }

    // this method should be overridden by specific players in order to
    // do additional upgrades for the player after level up
    public String postLevelingUp(MessageCallback cb, int deltaHealth, int deltaAttack, int deltaDefense) {
        String msg = this.name + " reached level " + this.level + ": +"
                + deltaHealth + " Health, +" + deltaAttack + " Attack, +"
                + deltaDefense + " Defense";

        return msg;
    }

    public void handlePlayerWin(int enemyXPValue, MessageCallback cb){
        this.experience = this.experience + enemyXPValue;
        // check if we gained enough points to get to the next level
        int totalXPAboveCurrentLevel = this.experience-(50*this.level);
        while (totalXPAboveCurrentLevel >= 0) {
            this.experience = totalXPAboveCurrentLevel;
            this.level++;

            int gainHealth = this.health.getHealthPool() + (10 * this.level);
            int deltaHealth = gainHealth - this.health.getHealthPool();
            this.health.setHealthPool(gainHealth);
            this.health.setHealthAmount(this.health.getHealthPool());

            int gainAttack = this.attackPoints + (4*this.level);
            int deltaAttack = gainAttack - this.attackPoints;
            this.attackPoints = gainAttack;

            int gainDefense = this.defensePoints + (this.level);
            int deltaDefense = gainDefense - this.defensePoints;
            this.defensePoints = gainDefense;

            var msg = postLevelingUp(cb, deltaHealth, deltaAttack, deltaDefense);
            cb.send(msg);

            // again, check if we gained enough points to get to the next level
            totalXPAboveCurrentLevel = this.experience - (50 * this.level);
        }
    }

    public boolean isAlive() {
        return this.isAlive;
    }
    @Override
    public void accept(TileVisitor visitor) {
        visitor.visit(this);
    }

    public String StringPlayerStats(){
        String spaces = "                ";
        String playerStats = this.name +spaces+ "Health: " +
                this.health.getHealthAmount() +"/" +this.health.getHealthPool() +
                spaces+ "Attack: "+ this.attackPoints +
                spaces+ "Defense: " +this.defensePoints +
                spaces+ "Level: " +this.level +
                spaces+ "Experience: " +
                this.experience + "/" + this.level*50 +spaces;
        return playerStats;
    }


    @Override
    public char getTile() {
        return isAlive() ? super.getTile(): 'X';
    }

    public void setDead() {
        isAlive = false;
    }

    public void printStats(MessageCallback callback) {
        callback.send(this.StringPlayerStats());
    }

    // this method is called after a player and enemy has performed an action
    public void OnGameRound() {
    }

    //for tests
    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }
}
