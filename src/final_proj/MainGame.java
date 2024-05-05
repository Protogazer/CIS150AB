package final_proj;

import java.util.Scanner;

public class MainGame {
    /* 
     * The one and only non-private variable. This var is not public, and can only be accessed
     * by other classes inside the package. It's used as a flag signaling keypresses from the KeyListener thread
     * that can interrupt the gameWorld updater when the user wants to make changes.
     * There's probably a better way to do it, but I couldn't figure it out in time
     */
    static volatile boolean keyPressed = false;

    public static void main(String[] args) {
        // TODO include calendar system?
        // TODO make class for user inputs that encapsulates all methods?
        Scanner input = new Scanner(System.in);
        String playerName = "";
        String shipName = "";
        String destinaiton = "";
        PlayerShip player;
        int timeElapsed = -1;

        int choiceNumber = -1;
        int numberOfOptions = -1;
        int distance = -1;

        /* 
         * Creates a separate thread for listening to keyboard inputs while the world is updating
         * executing this in its own thread allows for interruption and continuaiton
         * reference: https://stackoverflow.com/questions/19489054/break-continuous-loop-with-key  
         */
        KeyListener userInputListener = new KeyListener(input);
        Thread t = new Thread(userInputListener);

        // prices (set these when encountering a new shop, then I can resue the dialog trees)
        int oxygenPrice = -1;
        int powerChargePrice = -1;
        int powerCellPrice = -1;
        int rationsPrice = -1;
        int shieldUpgradePrice = -1;

        clearScreen();

        System.out.print("What is your name? ");
        playerName = input.nextLine();

        System.out.print("What is the name of your spaceship? ");
        shipName = input.nextLine();
        
        // set game length
        System.out.println("Where would you like to travel to? (type an option number and hit ENTER)");
        System.out.println("(1) Interplanetary Base [Short game] \n(2) Mars [Medium game] \n(3) Ganymede [Long gmae] \n(4) Titan [Very Long game]");
        numberOfOptions = 4;
        choiceNumber = getChoiceNumber(input, numberOfOptions);

        switch (choiceNumber) {
            case 1:
                distance = 75000000;
                destinaiton = "Interplanetary Base 8675-B";
                break;
            case 2:
                distance = 140000000;
                destinaiton = "Mars";
                break;
            case 3:
                distance = 390000000;
                destinaiton = "Ganymede";
                break;
            case 4:
                distance = 746000000;
                destinaiton = "Titan";
                break;
            default:
            // default to Mars
                distance = 140000000;
                destinaiton = "Mars";
                break;
        }

        player = new PlayerShip(playerName, shipName, distance);

        // PlayerShip player = new PlayerShip(playerName, shipName, milesToDestinaiton);
        clearScreen();

        // START GREETING
        System.out.println("Welcome captain " + playerName +"! You've finally gone and done it, you've bought your own starship named " + shipName + ". You've charted a course for " + destinaiton + " with a distance of " + distance + " miles.\nBuying the ship has left you with only 1000 credits to your name and you haven't even bought your supplies yet! You head to Bright Side -the largest pitstop this side of the moon- in order to get supplies.\n");

        advanceText(input);

        System.out.println("As you enter Bright Side, a frail hunched figure, more wrinkles than man, slowly shuffles to greet you.\n\nShopkeep:\n\"Howdy! *cough* I see you've got a new ship. So they finally threw that hunk of junk away did they? Anyhow, if your here that means you need supplies. I think I've got just the ticket.\"\n");

        System.out.println("\nPress ENTER to continue.");
        input.nextLine();
        clearScreen();

        // SHOP INTERACTION CODE BELOW
        // LIST CURRENT STATS
        System.out.println(player.getAllStats() + "\n\n\"Here's what I've got:\"");
        
        // ITEM PRICES
        powerChargePrice = 50;
        powerCellPrice = 35;
        rationsPrice = 3;
        shieldUpgradePrice = 650;
        oxygenPrice = (int) Math.round(Math.abs(player.getOxygen() - 100) * 2);
        powerChargePrice = (int) Math.round(Math.abs(player.getPower() - player.getPowerCapacity()) * 1.8); // calculated based on delta between current capacity and max

        // PRINT ITEM LIST
        System.out.println("(1) Oxygen refills - " + oxygenPrice + " Credits to completely fill\n(2) Power Cells (increase range) - " + powerCellPrice + " Credits each\n(3) Rations - " + rationsPrice + " Credits per pound\n(4) Upgraded Shields - " + shieldUpgradePrice + " Credits\n(5) Charge power cells - "+ powerChargePrice +" Credits to fully charge\n(6) Leave the shop");
        numberOfOptions = 6;
        choiceNumber = getChoiceNumber(input, numberOfOptions);

        while (choiceNumber != 6) {
            clearScreen();
            switch (choiceNumber) {
                case 1:
                    System.out.print("Do you want to refill your O2 tanks? You've got " + player.getOxygen() + "% left in the tank. It'll cost ya "+ oxygenPrice +"\n(Enter YES or NO): ");
                    player.buyItems("oxygen", getYesNo(input), oxygenPrice);
                    break;
                case 2:
                    System.out.print("How many power cells do ya want? - " + powerCellPrice + " Credits per cell\n(Type number and hit ENTER): ");
                    player.buyItems("power", input.nextInt(), powerCellPrice);
                    break;
                case 3:
                    System.out.print("How many pounds of food do ya want? " + rationsPrice + " Credits per pound\n(Typer number and hit ENTER): ");
                    player.buyItems("rations", input.nextInt(), rationsPrice);
                    break;
                case 4:
                    System.out.print("So ya wanna upgrade that weak shield huh? That'll be "+ shieldUpgradePrice +" (Enter YES or NO): ");
                    player.buyItems("shields", getYesNo(input), shieldUpgradePrice);
                    break;
                case 5:
                    System.out.print("I can charge those batteries up for ya if you want. You've currently got a charge of " + player.getBatteryPercentage() + "%.\nThat'll cost ya " + powerChargePrice + " Credits. What do ya say?\n(Enter YES or NO): ");
                    player.buyItems("charge", getYesNo(input), powerChargePrice);
                break;
                case 6:
                    System.out.println("Alright, see ya around kid!");
                default:
                    System.out.println("We don't got that here.");
                    break;
            }
            System.out.println(player.getAllStats());
            System.out.println("\nAnything else ya need?");
            
            // Update variable prices
            oxygenPrice = (int) Math.round(Math.abs(player.getOxygen() - 100) * 2);
            powerChargePrice = (int) Math.round(Math.abs(player.getPower() - player.getPowerCapacity()) * 1.8); // calculated based on delta between current capacity and max

            System.out.println("(1) Oxygen refills - " + oxygenPrice + " Credits to completely fill\n(2) Power Cells (increase range) - " + powerCellPrice + " Credits each\n(3) Rations - " + rationsPrice + " Credits per pound\n(4) Upgraded Shields - " + shieldUpgradePrice + " Credits\n(5) Charge power cells - "+ powerChargePrice +" Credits to fully charge\n(6) Leave the shop");
            choiceNumber = getChoiceNumber(input, numberOfOptions);
        }

        // initialize world variables
        timeElapsed = 0;    // resets timer count
        t.start();          // key listener thread


        // WORLD UPDATER
        while (distance > 0){
            clearScreen();
            int warningFlag = 0;

            // check the flag
            if (keyPressed == true) {
                // run menu subroutine
                menu(input, player);
                // resets flags and launches new KeyListener thread
                resetKeyListener(userInputListener);
            }

            // update every day until destinaiton is reached
            System.out.println(player.getAllStats() + "\n\nCurrent Speed: " + player.getMilesPerDay()/24 + " MPH");
            distance -= player.getMilesPerDay();
            warningFlag = player.updateStatus();
            timeElapsed++;
            System.out.println("Miles to " + destinaiton + ": " + distance + " (" + distance/player.getMilesPerDay() +" Days)");
            System.out.println("Days in space: " + timeElapsed);

            // TODO Roll event for if statement. Returns positive int if an event occurs
            // add the return to the warning flag

            // check warning flag and pause update if something needs to be addressed
            if (warningFlag > 0) {
                while (keyPressed == false) {
                    // wait until key is pressed
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                clearScreen();

                // TODO print results of event
                System.out.println("Okay the event sucked you lost this shit:\n\n-1 Oxygen\n-2 Rations");
                
                advanceText(input);

                resetKeyListener(userInputListener);

                // start next day (MAY NOT USE THIS REALLY)
                continue;
            } else {
                System.out.println("\nAll Clear.");
            }
            System.out.println("\nPress ENTER to make operational changes for your ship.");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (distance <= 0) {
                // while loop will exit automatically
                System.out.println("You made it!");
            }
        }
    }

    private static void advanceText(Scanner input) {
        System.out.println("\nPress ENTER to continue.");
        input.nextLine();
    }

    private static void clearScreen(){
        System.out.print("\033\143"); // clears the terminal in vscode (according to https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java)
    }

    // when called, get the user's input and the number of options available for the given choice branch
    // choices are NOT 0 indexed, they will always start with 1.
    private static int getChoiceNumber(Scanner input, int numberOfOptions){
        int userInput = input.nextInt();
        while (userInput > numberOfOptions || userInput <= 0) {
            System.out.print("That choice isn't available. Please pick an option between 1 and " + numberOfOptions + ": ");
            userInput = input.nextInt();
        }
        input.nextLine(); // eat newline
        return userInput;
    }

    // get user input for YES and NO, return 1 for YES, 0 for NO
    private static int getYesNo(Scanner input){
        String yesOrNo = input.nextLine();
        yesOrNo = yesOrNo.toUpperCase();

        while (!yesOrNo.equals("YES") && !yesOrNo.equals("NO")) {
            System.out.println("I've got no clue what that means! I think my translator is busted. What'd you say?");
            yesOrNo = input.nextLine();
            yesOrNo = yesOrNo.toUpperCase();
        }

        if (yesOrNo.equals("YES")) {
            return 1;
        } else if (yesOrNo.equals("NO")) {
            return 0;
        } else {
            System.err.println("Unable to resolve input");
            return -1;
        }
    }

    private static void menu (Scanner input, PlayerShip player){
        int menuChoice = -1;
        int choice2 = -1;
        // allow changed to the ship from here
        System.out.println("Would you like to make changes to your ship?\n\n(1) Adjust Speed\n(2) Change meal plan\n(3) Toggle Shields\n(4) Help\n(5) Exit Menu\n");
        System.out.print("Choose an option: ");
        menuChoice = getChoiceNumber(input, 5);

        switch (menuChoice) {
            case 1:
                System.out.println("\nYou engine speed is currently set to "+ player.getEngineThrust());
                System.out.println("What speed would you like to set it to?\n(1) Off (+2 charge/day)\n(2) Low ("+player.getEnginePowerDraw(1)+" charge/day)\n(3) Medium ("+ player.getEnginePowerDraw(2) +" charge/day)\n(4) High ("+ player.getEnginePowerDraw(3)+" charge/day)");
                choice2 = getChoiceNumber(input, 4);
                player.setEngineSpeed(choice2 -1); // EngineSpeed is 0 indexed in PlayerShip class
                clearScreen();
                // returns you to the menu until you intentionally exit (option 5)
                System.out.println("Engine speed set to " + player.getEngineThrust() + "\n");
                menu(input, player);
                break;
            case 2:
                System.out.println("World");
                // returns you to the menu until you intentionally exit (option 5)
                menu(input, player);
                break;
            case 3:
                System.out.println("Alex");
                // returns you to the menu until you intentionally exit (option 5)
                menu(input, player);
                break;
            case 4:
                System.out.println("\nHELP:\n\nThe engine uses power cells in order to run. The it takes much more power to run as speed increases, so it's best only use it when necessary. Your ship has solar power, and can slowly recharge if the engine is left off. Since you are in space, you will retain some forward momentum even when the engines are off.\n\nYour meal plan determines how quickly you will go through your rations. The less you eat however, the more often you may get sick, so keep that in mind.\n\nThe shields are on by default, and protect against small amounts of space debris and other minor damage. However the shield draws power too and can be disabled to help offset battery draining.");
                advanceText(input);
                clearScreen();

                // returns you to the menu until you intentionally exit (option 5)
                menu(input, player);
                break;
            case 5:
                clearScreen();
                System.out.println("Now leaving the bridge...\n");
                break;

            default:
                break;
        }
    }

    private static void resetKeyListener (KeyListener userInputListener){
            // set keyPressed back to false
            keyPressed = false;
            // restart new listener thread
            Thread t2 = new Thread(userInputListener);
            t2.start();
            return;
    }
}
