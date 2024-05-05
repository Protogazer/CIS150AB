package final_proj;

/*
 * Alex Johnson
 * Final Project KeyListener
 * 
 * This class implements Runnable, allowing it to be run in a separate thread
 * this was necessary in order to allow for interrupting the world update when the player
 * wanted to make changes. Otherwise the updated would need to prompt the user every single
 * time it ran through the loop, or the user wouldn't be able to pause the updater on their own.
 * 
 * in the run() method, the input scanner is set to listen for any event ending in a new line and then
 * setting the global MainGame.keyPressed bool to true. It gets reset back in MainGame.
 */

import java.util.Scanner;

public class KeyListener implements Runnable{

    private Scanner userInput;


    public KeyListener(Scanner input) {
        userInput = input;
    }

    @Override
    public void run() {
        String input = userInput.nextLine();
        if (input.isEmpty() || !input.isEmpty()) { // stops counter even if you accidentally entered a character before hitting ENTER
            // set world update flag
            System.out.println("Walking towards the bridge...\n");
            MainGame.keyPressed = true; // this was the simplest workaround I could find. Not my best work.
        }
    }
}
