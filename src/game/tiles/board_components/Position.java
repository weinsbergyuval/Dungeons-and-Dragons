package game.tiles.board_components;

import java.util.Objects;
import java.util.Random;

public class Position {
    protected int row;
    protected int col;

    public enum Direction {
        NONE, UP, DOWN, LEFT, RIGHT, RANDOM
    }

    public int Row() {
        return this.row;
    }

    public int Col() {
        return this.col;
    }

    public Position RightPosition()  {
        return new Position(row, col+1);
    }

    public Position LeftPosition()  {
        return new Position(row, col-1);
    }

    public Position UpPosition()  {
        return new Position(row-1, col);
    }

    public Position DownPosition()  {
        return new Position(row+1, col);
    }

    public Position GetTargetDirection(Direction dir) {
        Position pos = null;
        switch (dir) {
            case NONE -> {
                pos = this;
            }
            case UP -> {
                 pos=UpPosition();
            }
            case DOWN -> {
                pos=DownPosition();
            }
            case LEFT -> {
                pos = LeftPosition();
            }
            case RIGHT -> {
                pos = RightPosition();
            }
            case RANDOM -> {
                Direction[] directions = Direction.values();
                Random random = new Random();
                int index = random.nextInt(5); // Get a random index between 0 and 4
                Direction chosenDirection = directions[index];
                if (chosenDirection != Direction.RANDOM) {
                    // just in case..
                    return GetTargetDirection(chosenDirection);
                }

                return this;
            }
        }

        return pos;
    }

    //range between to tiles
    public double range(Position otherPos){
        int dx = this.row - otherPos.getRow();
        int dy = this.col - otherPos.getCol();
        return Math.sqrt(dx*dx + dy*dy);
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public Position(int row,int col){
        this.row = row;
        this.col = col;
    }

    public Position translate(char command) {
        Position newPos = null;
        switch (command) {
            case 'w':
                newPos = GetTargetDirection(Direction.UP);
                break;
            case 's':
                newPos = GetTargetDirection(Direction.DOWN);
                break;
            case 'a':
                newPos = GetTargetDirection(Direction.LEFT);
                break;
            case 'd':
                newPos = GetTargetDirection(Direction.RIGHT);
                break;
            case 'q': // stay in place
                newPos = GetTargetDirection(Direction.NONE);
                break;
            default:
                newPos = GetTargetDirection(Direction.NONE);;
        }
        return newPos;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Position position)) return false;

        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
