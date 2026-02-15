package game;

import game.tiles.Tile;
import game.tiles.board_components.Position;
import game.tiles.units.Health;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.*;
import view.CLI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Game {

    private Board board;
    private final CLI cli;

    public Game(CLI cli) {
        this.cli = cli;
    }

    public Player createPlayer(char playerNumber){
        Player p = null;
        if (playerNumber == '1'){
            p = new Warrior("John Snow", null, new Health(300, 300) , 30, 4, 3);
        }
        if (playerNumber == '2'){
            p= new Warrior("The Hound", null, new Health(400, 400), 20, 6, 5);
        }
        if (playerNumber == '3'){
            p=  new Mage("Melisandre", null, new Health(100, 100) , 5, 1, 300, 30, 15, 5, 6);
        }
        if (playerNumber == '4'){
            p=   new Mage("Thoros of Myr", null, new Health(250, 250), 25, 4, 150, 20, 20, 3, 4);
        }
        if (playerNumber == '5'){
            p=   new Rogue("Arya Stark", null, new Health(150, 150), 40, 2, 20);
        }
        if (playerNumber == '6'){
            p = new Rogue("Bronn", null, new Health(250, 250), 35, 3, 50);
        }
        if (playerNumber == '7'){
            p = new Hunter("Ygritte", null, new Health(220, 220), 30, 2, 6);
        }

        return p;
    }

    private boolean checkIfNextLevelExists(int level) {
        // create the level filepath
        Path levelPath = Paths.get(cli.getLevelsDirectory(), "level" +level+ ".txt");
        String boardLevelFileName = levelPath.toString();
        File boardFile = new File(boardLevelFileName);
        return boardFile.exists();
    }

    private Board createBoardForLevel(int level, Player player) {
        // create the level filepath
        Path levelPath = Paths.get(cli.getLevelsDirectory(), "level" +level+ ".txt");
        String boardLevelFileName = levelPath.toString();

        if (checkIfNextLevelExists(level)) {
            return new Board(player, boardLevelFileName);
        }

        CLI.DisplayMessage("The board level file: '" + boardLevelFileName + "' does NOT exist.");
        return null;
    }

    private int level = 1;

    public void run() {
        cli.displayPlayers();
        char input = CLI.getUserPlayerSelection('1','7');
        Player player = createPlayer(input);

        cli.printPlayerSelected(player);

        this.board = createBoardForLevel(level, player);
        if (this.board == null) {
            CLI.DisplayMessage("Error starting game, check that levels directory path has levels..."
                    + "(" + cli.getLevelsDirectory() + ")");
            System.exit(1);
        }

        boolean gameOver = false;
        while (!gameOver) {
            cli.displayBoard(board);
            cli.printStats(board.getPlayer());

            // (1) handle player move
            char cmd = CLI.getDirectionChar();
            if (cmd == '0'){//quit game
                System.exit(0);
            }

            // a cast does not change the player position
            if (cmd == 'e'){
                // cast action of the player
                cli.castSpecialAbility(player, board); //if player cannot cast - continues to next turn
            } else {
                // movement action of the player
                // we create a PlayerMovementVisitor that represents the player
                // that visits different tiles
                Tile targetTile = board.getTargetTile(player.getPosition(), cmd);
                targetTile.accept(new PlayerMovementVisitor(player, board));
            }

            // (2) enemy action
            for (Enemy enemy: board.getEnemies()) {
                // let the enemy take a turn, provide it the player position so he
                // can choose to chase it. The next enemy tile is returned
                Position nextPosition = enemy.onTurn(player);
                Tile enemyTargetTile = board.getTileAt(nextPosition);
                enemyTargetTile.accept(new EnemyMovementVisitor(enemy, board));
            }

            // notify all players and enemies tht a round is done
            player.OnGameRound();

            var playerIsDead = checkIfPlayerIsDead(player);
            if (!playerIsDead) {
                if (board.allEnemiesDead()) {
                    level++;
                    boolean isNextLevel = checkIfNextLevelExists(level);
                    if (isNextLevel) {
                        this.board = createBoardForLevel(level, player);
                        CLI.DisplayMessage("all enemies are dead, level completed!");
                    } else {
                        CLI.DisplayMessage("You Win !!! Game Completed :-)");
                        CLI.DisplayMessage(cli.StringGameOver());
                        gameOver = true;
                    }
                }
            } else {
                CLI.DisplayMessage(cli.StringYouLost());
                gameOver = true;
            }
        }
    }

    private boolean checkIfPlayerIsDead(Player player) {
        if (!board.getPlayer().isAlive()) {
            cli.displayBoard(board);
            cli.printStats(player);
            return true;
        }

        return false;
    }
}
