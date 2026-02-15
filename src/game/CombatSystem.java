package game;

import java.util.Random;

import game.callbacks.ClearSightCallback;
import game.callbacks.MessageCallback;
import game.tiles.board_components.Position;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;

public class CombatSystem {

    // this method should handle the case where a player is interacting with an enemy
    public static void handlePlayerAttacksEnemy(Player player, Enemy enemy, Board board, MessageCallback cb) {
        cb.send(player.getName() + " engaged in combat with " +enemy.getName()+ ".");
        player.printStats(cb);
        enemy.printStats(cb);

        int damage = player.RollAttackPoints(cb) - enemy.RollDefensePoints(cb);
        if (damage > 0) {
            enemy.getHealth().decreaseHealth(damage);
            cb.send(player.getName() + " dealt " + damage + " damage to " + enemy.getName() + ".");
            checkAndHandleDeadEnemy(player, enemy, board, cb);
        }
        else{
            cb.send(player.getName() + " dealt 0 damage to " + enemy.getName() + ".");
        }
    }

    private static void checkAndHandleDeadEnemy(Player player, Enemy enemy, Board board, MessageCallback cb) {
        if (enemy.isDead()){
            cb.send(enemy.getName() + " died. " +player.getName()+ " gained " +enemy.getExperienceValue()+ " experience");
            player.handlePlayerWin(enemy.getExperienceValue(),cb);
            board.removeEnemy(enemy);
        }
    }

    public static void handleEnemyAttacksPlayer(Enemy enemy, Player player, Board board, MessageCallback cb) {
        cb.send(enemy.getName() + " engaged in combat with " +player.getName()+ ".");
        enemy.printStats(cb);
        player.printStats(cb);

        Random random = new Random();
        int attackRoll = random.nextInt(enemy.getAttackPoints() + 1); // [0, attackPoints]
        int defenseRoll = random.nextInt(player.getDefensePoints() + 1); // [0, defensePoints]
        int damage = attackRoll - defenseRoll;

        cb.send(enemy.getName() + " rolled " + attackRoll + " attack points.");
        cb.send(player.getName() + " rolled " + defenseRoll + " defense points.");

        if (damage > 0) {
            player.getHealth().decreaseHealth(damage);
            cb.send(enemy.getName() + " dealt " + damage + " damage to " + player.getName() + ".");
            if (player.getHealth().getHealthAmount() <= 0){
                cb.send(player.getName() + " was killed by " +enemy.getName()+ ".\n");
                board.markPlayerAsDead(player);
            }
        }
        else{
            cb.send(player.getName() + " dealt 0 damage to " + enemy.getName() + ".");
        }
    }


    // this method should handle the case where a player is activating special ability
    public static void handlePlayerSpecialAbilityAttack(Player player, Board board, MessageCallback cb) {
        var enemies =  board.getEnemies(); //if player cannot cast - continues to next turn

        ClearSightCallback cleanSightCallback = (Position from, Position to) -> {
            return board.hasClearSight(from,to);
        };

        player.castAbility(cb, cleanSightCallback, enemies, player);

        // since the cast can make some enemies dead, we handle it
        Enemy[] toCheckEnemies = new Enemy[enemies.size()];
        enemies.toArray(toCheckEnemies);
        for (Enemy e : toCheckEnemies) {
            checkAndHandleDeadEnemy(player, e, board, cb);
        };
    }
}


