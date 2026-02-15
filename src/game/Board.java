package game;

import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_components.Empty;
import game.tiles.board_components.Position;
import game.tiles.board_components.Wall;
import game.tiles.units.Health;
import game.tiles.units.enemy.Boss;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.enemy.Monster;
import game.tiles.units.enemy.Trap;
import game.tiles.units.player.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Board {

    private Tile[][] grid;
    private int rows, cols;
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();

    protected Board(Player player, String boardFileName) {
        this.player = player;
        this.grid = this.loadLevelFromFile(boardFileName);
        if (this.grid == null){
            System.out.println("error loading level from file - please check the file or code");
            System.exit(-1);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Board(String boardFileName, Player player) {
        this.player = player;

    }

    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    // using google search, it seems that Bresenham’s Line Algorithm is a nice
    // algorithm to try...
    public boolean hasClearSight(Position from, Position to) {
        int x0 = from.Col();
        int y0 = from.Row();
        int x1 = to.Col();
        int y1 = to.Row();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (!(x0 == x1 && y0 == y1)) {
            // Skip checking the start and end positions themselves
            if (!(x0 == from.Col() && y0 == from.Row()) && !(x0 == to.Col() && y0 == to.Row())) {
                Tile tile = this.grid[y0][x0]; // Note: y = row, x = column
                if (tile.getTile() == '#') {
                    return false;
                }
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }
        }

        return true;
    }


    public Tile[][] loadLevelFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        if (lines.isEmpty()) {
            return null;
        }

        this.rows = lines.size();
        this.cols = lines.getFirst().length(); // assume all rows have the same length


        Tile[][] tiles = new Tile[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            if (line.length() != cols) {
                System.err.println("Warning: Row " + (i + 1) + " has inconsistent length.");
                continue;
            }
            for (int j = 0; j < cols; j++) {
                char symbol = line.charAt(j);
                Position position = new Position(i, j); // x (rows), y (columns)
                Monster m;
                Trap t;
                Boss b;
                switch (symbol) {
                    case '#':
                        tiles[i][j] = new Wall(position);
                        break;
                    case '.':
                        tiles[i][j] = new Empty(position);
                        break;
                    case '@':
                        tiles[i][j] = this.player;
                        player.setPosition(position);
                        break;
                    case 's':
                        //Lannister Solider
                        m = new Monster("Lannister Solider", symbol, position,
                                new Health(80, 80),
                                8, 3,
                                3, 25);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'k':
                        //Lannister Knight
                        m = new Monster("Lannister Knight", symbol, position,
                                new Health(200, 200),
                                14, 8,
                                4, 50);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'q':
                        //Queen's Guard
                        m = new Monster("Queen's Guard", symbol, position,
                                new Health(400, 400),
                                20, 15,
                                5, 100);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'z':
                        //Wright
                        m = new Monster("Wright", symbol, position,
                                new Health(600, 600),
                                30, 15,
                                3, 100);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'b':
                        //Bear-Wright
                        m = new Monster("Bear-Wright", symbol, position,
                                new Health(1000, 1000),
                                75, 30,
                                4, 250);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'g':
                        //Giant-Wright
                        m = new Monster("Giant-Wright", symbol, position,
                                new Health(1500, 1500),
                                100, 40,
                                5, 500);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'w':
                        //White Walker
                        m = new Monster("White Walker", symbol, position,
                                new Health(2000, 2000),
                                150, 50,
                                6, 1000);
                        tiles[i][j] = m;
                        enemies.add(m);
                        break;
                    case 'M':
                        //The Mountain
                        b = new Boss("The Mountain", symbol, position,
                                new Health(1000, 1000),
                                60, 25,
                                6, 500, 5);
                        tiles[i][j] = b;
                        enemies.add(b);
                        break;
                    case 'C':
                        //Queen Cersei
                        b = new Boss("Queen Cersei", symbol, position,
                                new Health(100, 100),
                                10, 10,
                                1, 1000, 8);
                        tiles[i][j] = b;
                        enemies.add(b);
                        break;
                    case 'K':
                        //Night's King
                        b = new Boss("Night's King", symbol, position,
                                new Health(5000, 5000),
                                300, 150,
                                8, 5000, 3);
                        tiles[i][j] = b;
                        enemies.add(b);
                        break;
                    case 'B': //todo
                        //Bonus Trap
                        t = new Trap("Bonus Trap", symbol, position,
                                new Health(1, 1),
                                1, 1,
                                250, 1, 5);
                        tiles[i][j] = t;
                        enemies.add(t);
                        break;
                    case 'Q':
                        //Queen's Trap
                        t = new Trap("Queen's Trap", symbol, position,
                                new Health(250, 250),
                                50, 10,
                                100, 3, 7);
                        tiles[i][j] = t;
                        enemies.add(t);
                        break;
                    case 'D':
                        //Death Trap
                        t = new Trap("Death Trap", symbol, position,
                                new Health(500, 500),
                                100, 20,
                                250, 1, 10);
                        tiles[i][j] = t;
                        enemies.add(t);
                        break;
                    default:
                        tiles[i][j] = new Empty(position); // Default to empty if unknown
                        break;
                }
            }
        }
        return tiles;
    }

    public Tile getTileAt(Position position) {
        return grid[position.Row()][position.Col()];
    }

    public void setTileAt(Position position, Tile tile) {
        grid[position.Row()][position.Col()] = tile;
    }

    public void moveTile(Tile tile, Position newPosition) {
        // put an empty tile in the current tile position
        setTileAt(tile.getPosition(), new Empty(tile.getPosition()));

        // set the tile's new position
        tile.setPosition(newPosition);

        // set the tile at the new position
        setTileAt(newPosition, tile);
    }

    public void draw(MessageCallback cb) {
        StringBuilder boardString = new StringBuilder();
        for (Tile[] row : grid) {
            for (Tile t : row) {
                boardString.append(t.getTile()).append(" ");
            }
            boardString.append("\n");
        }
        cb.send(boardString.toString());
    }

    public Tile getTargetTile(Position position, char cmd) {
        Position targetPosition = position.translate(cmd);
        return getTileAt(targetPosition);
    }

    public boolean allEnemiesDead() {
        if (enemies.isEmpty()) {
            return true;
        }
        return false;
    }

    public void removeEnemy(Enemy enemy) {
        this.enemies.remove(enemy);
        setTileAt(enemy.getPosition(), new Empty(enemy.getPosition()));
    }

    public void markPlayerAsDead(Player player) {
        this.player.setDead();
    }
}
