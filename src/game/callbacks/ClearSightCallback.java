package game.callbacks;

import game.tiles.board_components.Position;

public interface ClearSightCallback {
    boolean hasClearSight(Position from, Position to);
}
