package game;

import game.tiles.board_components.Empty;
import game.tiles.board_components.Wall;
import game.tiles.units.TileVisitor;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;
import view.CLI;

// The PlayerMovementVisitor represents a player that visits
// different tiles (visits another player, enemy, wall, etc.)
public class PlayerMovementVisitor implements TileVisitor {
    private final Player player;
    private final Board board;

    public PlayerMovementVisitor(Player player, Board board) {
        this.player = player;
        this.board = board;
    }

    @Override
    public void visit(Player otherPlayer) {
        // do nothing, we are single player
    }

    @Override
    public void visit(Enemy enemy) {
        CombatSystem.handlePlayerAttacksEnemy(player, enemy, board, CLI::DisplayMessage);
        // check whether we need to move the player to the enemy position
        // we only do that if the enemy is dead and the player is adjacent to the enemy
        if (enemy.isDead()) {
            if (player.getPosition().range(enemy.getPosition()) == 1) {
                board.moveTile(player, enemy.getPosition());
            }
        }
    }

    @Override
    public void visit(Empty empty) {
        // move the player to the empty position
        board.moveTile(player, empty.getPosition());
    }

    @Override
    public void visit(Wall wall) {
        // do nothing, can not move
    }
}
