package game.tiles.units;

public class Health {
    protected int healthPool;
    protected int healthAmount;

    public Health(int healthPool, int healthAmount){
        this.healthPool = healthPool;
        this.healthAmount = healthAmount;
    }

    public void addHealth(int healthToAdd){
        this.healthAmount = Math.min(this.healthAmount + healthToAdd, this.healthPool);
    }

    public void decreaseHealth(int healthToDecrease){
        this.healthAmount = Math.max(0, this.healthAmount - healthToDecrease);
    }

    public int getHealthPool(){
        return this.healthPool;
    }

    public void setHealthPool(int newHealthPool){
        this.healthPool = newHealthPool;
    }

    public void setHealthAmount(int newHealthAmount){
        this.healthAmount = newHealthAmount;
    }

    public int getHealthAmount(){
        return this.healthAmount;
    }
}
