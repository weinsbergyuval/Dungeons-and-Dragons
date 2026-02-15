package game.tiles.board_components;

import game.tiles.Tile;
import game.tiles.units.TileVisitor;

public class Empty extends Tile {
    public Empty(Position position) {
        super('.', position);
    }

    @Override
    public void accept(TileVisitor visitor) {
        visitor.visit(this);
    }
}
