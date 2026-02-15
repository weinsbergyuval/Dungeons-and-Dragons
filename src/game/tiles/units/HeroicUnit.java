package game.tiles.units;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;

import java.util.List;

public interface HeroicUnit {
    public abstract void castAbility(MessageCallback cb,
                                     ClearSightCallback clearSightCallback,
                                     List<Enemy> enemies,
                                     Player player);
}
