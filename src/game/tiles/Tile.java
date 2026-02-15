package game.tiles;

import game.tiles.board_components.Position;
import game.tiles.units.TileVisitor;


public abstract class Tile {
    protected char tile;
    protected Position position;

    protected Tile(char tile, Position position){
        this.tile = tile;
        this.position = position;
    }

    public void setTile(char tile){
         this.tile = tile;
    }

    public char getTile(){
        return this.tile;
    }

    public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void accept(TileVisitor visitor);
}
