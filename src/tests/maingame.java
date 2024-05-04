package tests;

import java.util.Scanner;

import final_proj.WorldObject;

public class maingame {
    public static void main(String[] args) {
        boolean pollController = true;
        Scanner input = new Scanner(System.in);

        // create counting thread
        WorldObject gameWorld = new WorldObject(999,2);
        Thread worldThread = new Thread(gameWorld);

        // start thread
        worldThread.start();
        
        // start thread controller
        while (pollController) {
            String userInput = input.nextLine();
            if (userInput.isEmpty() || !userInput.isEmpty()) { // stops counter even if you accidentally entered a character
                gameWorld.stopCounter();
                pollController = false;
                break;
            }
            System.out.println("controllerloop");
        }

        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("RESTARTING COUNT");
        gameWorld.runCounter();
    }
}
