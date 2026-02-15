package game;

import game.tiles.board_components.Empty;
import game.tiles.board_components.Wall;
import game.tiles.units.TileVisitor;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;
import view.CLI;

// The EnemyMovementVisitor represents an enemy that visits
// different tiles (visits another player, enemy, wall, etc.)
public class EnemyMovementVisitor implements TileVisitor {
    private final Enemy enemy;
    private final Board board;

    public EnemyMovementVisitor(Enemy enemy, Board board) {
        this.enemy = enemy;
        this.board = board;
    }


    @Override
    public void visit(Player player) {
        CombatSystem.handleEnemyAttacksPlayer(enemy, player, board, CLI::DisplayMessage);
    }

    @Override
    public void visit(Enemy otherEnemy) {
        if (otherEnemy.equals(this.enemy)) {
            return;
        }

        // enemy can not move to another enemy
    }

    @Override
    public void visit(Empty empty) {
        board.moveTile(enemy, empty.getPosition());
    }

    @Override
    public void visit(Wall wall) {
        // enemy can not move to a wall
    }
}
