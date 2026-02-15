package view;

import game.Game;

public class Program {
    public static void main(String[] args) {
        if (args.length < 1){
            System.out.println("Error: this program needs a path to the levels directory as an argument.");
            System.exit(-1);
        }

        CLI cli = new CLI(args[0]);
        Game theGame = new Game(cli);
        theGame.run();
    }
}
