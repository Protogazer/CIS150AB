package final_proj;

import java.util.Scanner;

public class KeyListener implements Runnable{

    Scanner userInput;


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
