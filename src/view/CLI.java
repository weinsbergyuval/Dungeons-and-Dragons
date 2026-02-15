package view;

import game.Board;
import game.CombatSystem;
import game.callbacks.MessageCallback;
import game.tiles.units.enemy.Enemy;
import game.tiles.units.player.Player;

import java.util.Scanner;

public class CLI {

    private final String levelsDirectory;

    public CLI(String levelsDirectory) {
        this.levelsDirectory = levelsDirectory;
    }

    public String getLevelsDirectory() {
        return levelsDirectory;
    }

    public void displayBoard(Board board) {
        MessageCallback callback = (String message) -> {System.out.print(message); };
        board.draw(callback);
    }

    public void printStats(Player player) {
        MessageCallback callback = (String message) -> {
            System.out.print(message);
            System.out.println();
        };
        player.printStats(callback); //calls the function according to the type of player
    }

    public void displayEnemyStats(Enemy enemy) {
        MessageCallback callback = (String message) -> {
            System.out.print(message);
            System.out.println();
        };
        enemy.printStats(callback); //calls the function according to the type of player
    }

    // I prefer to use a string for players list Vs. creating an array of all players
    // and return the one chosen by the user. Not sure what is the best approach.
    public void displayPlayers() {
        String players =
                "Select player:\n" +
                "1. Jon Snow             Health: 300/300         Attack: 30              Defense: 4              Level: 1                Experience: 0/50                Cooldown: 0/3\n" +
                "2. The Hound            Health: 400/400         Attack: 20              Defense: 6              Level: 1                Experience: 0/50                Cooldown: 0/5\n" +
                "3. Melisandre           Health: 100/100         Attack: 5               Defense: 1              Level: 1                Experience: 0/50                Mana: 75/300            Spell Power: 15\n" +
                "4. Thoros of Myr        Health: 250/250         Attack: 25              Defense: 4              Level: 1                Experience: 0/50                Mana: 37/150            Spell Power: 20\n" +
                "5. Arya Stark           Health: 150/150         Attack: 40              Defense: 2              Level: 1                Experience: 0/50                Energy: 100/100\n" +
                "6. Bronn                Health: 250/250         Attack: 35              Defense: 3              Level: 1                Experience: 0/50                Energy: 100/100\n" +
                "7. Ygritte              Health: 220/220         Attack: 30              Defense: 2              Level: 1                Experience: 0/50                Arrows: 10              Range: 6\n";


        MessageCallback callback = (String message) -> {System.out.print(message); };
        callback.send(players);
    }

    public void printPlayerSelected(Player player){
        String playerSelected = "You have selected:\n" +player.getName() +"\n";

        MessageCallback callback = (String message) -> {System.out.print(message); };
        callback.send(playerSelected);
    }

    //user picks player input
    public static char getUserPlayerSelection(char minNumberChar, char maxNumberChar) {
        Scanner scanner = new Scanner(System.in);
        char inputChar = '\0'; //default
        boolean valid = false;

        while (!valid) {
            String input = scanner.nextLine();

            try {
                if (input.length() != 1 || input.charAt(0) < minNumberChar || input.charAt(0) > maxNumberChar) {
                    String msg = "Input must be a single character between: " +  minNumberChar + " and: " + maxNumberChar;
                    throw new IllegalArgumentException(msg);
                }

                inputChar = input.charAt(0);
                valid = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
        return inputChar;
    }

    //gets direction input from user
    public static char getDirectionChar() {
        Scanner scanner = new Scanner(System.in);
        char inputChar = '\0';
        boolean valid = false;
        String validChars = "asdwqe0";

        while (!valid) {
            String input = scanner.nextLine().toLowerCase();

            try {
                if (input.length() != 1 || validChars.indexOf(input.charAt(0)) == -1) {
                    throw new IllegalArgumentException("Input must be one of the characters: a, s, d, w, e, q. or 0 to Quit");
                }

                inputChar = input.charAt(0);
                valid = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
        return inputChar;
    }

    public void castSpecialAbility(Player player, Board board){
        CombatSystem.handlePlayerSpecialAbilityAttack(player, board, CLI::DisplayMessage);
    }

    public static void DisplayMessage(String s) {
        System.out.println(s);
    }

    public String StringGameOver(){
    return
              "      __     __            __          __ _         _       \n" +
            "      \\ \\   / /            \\ \\        / /(_)       | |      \n" +
            "       \\ \\_/ /___   _   _   \\ \\  /\\  / /  _  _ __  | |      \n" +
            "        \\   // _ \\ | | | |   \\ \\/  \\/ /  | || '_ \\ | |      \n" +
            "         | || (_) || |_| |    \\  /\\  /   | || | | ||_|      \n" +
            "         |_| \\___/  \\__,_|     \\/  \\/    |_||_| |_|(_)      \n" +
            "   _____                            ____                    \n" +
            "  / ____|                          / __ \\                   \n" +
            " | |  __   __ _  _ __ ___    ___  | |  | |__   __ ___  _ __ \n" +
            " | | |_ | / _` || '_ ` _ \\  / _ \\ | |  | |\\ \\ / // _ \\| '__|\n" +
            " | |__| || (_| || | | | | ||  __/ | |__| | \\ V /|  __/| |   \n" +
            "  \\_____| \\__,_||_| |_| |_| \\___|  \\____/   \\_/  \\___||_|   \n" +
            "                                                            \n" +
            "                                                            ";
    }

    public String StringYouLost() {
        return
                "     __     __             _                  _    _        \n" +
                "     \\ \\   / /            | |                | |  | |       \n" +
                "      \\ \\_/ /___   _   _  | |      ___   ___ | |_ | |       \n" +
                "       \\   // _ \\ | | | | | |     / _ \\ / __|| __|| |       \n" +
                "        | || (_) || |_| | | |____| (_) |\\__ \\| |_ |_|       \n" +
                "        |_| \\___/  \\__,_| |______|\\___/ |___/ \\__|(_)       \n" +
                "   _____                            ____                    \n" +
                "  / ____|                          / __ \\                   \n" +
                " | |  __   __ _  _ __ ___    ___  | |  | |__   __ ___  _ __ \n" +
                " | | |_ | / _` || '_ ` _ \\  / _ \\ | |  | |\\ \\ / // _ \\| '__|\n" +
                " | |__| || (_| || | | | | ||  __/ | |__| | \\ V /|  __/| |   \n" +
                "  \\_____| \\__,_||_| |_| |_| \\___|  \\____/   \\_/  \\___||_|   \n" +
                "                                                            \n" +
                "                                                            ";

    }
}
