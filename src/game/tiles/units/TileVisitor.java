package game.tiles.units;

import game.tiles.Tile;
import game.tiles.board_components.Empty;
import game.tiles.board_components.Wall;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;

public interface TileVisitor {
    void visit(Player tile);
    void visit(Enemy tile);
    void visit(Empty tile);
    void visit(Wall tile);
}
