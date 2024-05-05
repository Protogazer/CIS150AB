package final_proj;

/*
 * Alex Johnson
 * Final Project Main Game driver
 * 
 * Designed to play similar to the Oregon Trail. Excuse the jank, I started this this past Monday.
 * 
 * This drives the main UI for the game. It prompts the user, and recieves input.
 * The player/ship are created as a separate PlayerShip object, storing all information
 * about the health and stats of the player.
 * This file also instantiates an EventManager and randomly rolls to encounter an event from the array
 * of events within the Event manager.
 */

import java.util.Random;
import java.util.Scanner;

public class MainGame {
    /* 
     * The intentionally non-private variable. This var is not public, and can only be accessed
     * by other classes inside the package. It's used as a flag signaling keypresses from the KeyListener thread
     * that can interrupt the gameWorld updater when the user wants to make changes.
     * There's probably a better way to do it, but I couldn't figure it out in time
     */
    static volatile boolean keyPressed = false;

    public static void main(String[] args) {
        // TODO include calendar system?
        Scanner input = new Scanner(System.in);

        EventManager events = new EventManager();
        RandomEvent currentEvent;
        boolean isEvent = false;
        Random rng = new Random();
        int eventCounter = -1;

        // Create an array of travel shops for the trip in miles, relative to the remaining miles of the trip
        int pitStopsArray[];
        int stopsCounter = 0;

        PlayerShip player;
        String playerName = "";
        String shipName = "";
        String destinaiton = "";
        int timeElapsed = -1;

        int choiceNumber = -1;
        int numberOfOptions = -1;
        int distanceRemaining = -1;
        int distanceTraveled = -1;

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
                distanceRemaining = 75000000;
                destinaiton = "Interplanetary Base 8675-B";
                pitStopsArray = new int[] {47738652, 29477857, 11566785, 0};
                break;
            case 2:
                distanceRemaining = 140000000;
                destinaiton = "Mars";
                pitStopsArray = new int[] {100418333, 66598721, 22680764, 9563300, 0};
                break;
            case 3:
                distanceRemaining = 390000000;
                destinaiton = "Ganymede";
                pitStopsArray = new int[] {302019056,248304940,174074346,81567408,200763010, 0};
                break;
            case 4:
                distanceRemaining = 746000000;
                destinaiton = "Titan";
                pitStopsArray = new int[] {686980315,591475672, 341317987, 211072086, 98476766, 38683111, 17568813, 4982812, 0};
                break;
            default:
            // default to Mars
                distanceRemaining = 140000000;
                destinaiton = "Mars";
                pitStopsArray = new int[] {100418333, 66598721, 22680764, 9563300, 0};
                break;
        }

        player = new PlayerShip(playerName, shipName, distanceRemaining);

        // PlayerShip player = new PlayerShip(playerName, shipName, milesToDestinaiton);
        clearScreen();

        // START GREETING
        System.out.println("Welcome captain " + playerName +"! You've finally gone and done it, you've bought your own starship named " + shipName + ". You've charted a course for " + destinaiton + " with a distance of " + distanceRemaining + " miles.\nBuying the ship has left you with only "+ player.getCredits() +" Credits to your name and you haven't even bought your supplies yet! You head to Bright Side -the largest pitstop this side of the moon- in order to get supplies.\n");

        advanceText(input);
        clearScreen();

        System.out.println("As you enter Bright Side, a frail hunched figure, more wrinkles than man, slowly shuffles to greet you.\n\nShopkeep:\n\"Howdy! *cough* I see you've got a new ship. So they finally threw that hunk of junk away did they? Anyhow, if your here that means you need supplies. I think I've got just the ticket.\"\n");

        System.out.println("\nPress ENTER to continue.");
        input.nextLine();
        clearScreen();

        // SHOP INTERACTION CODE BELOW
        // LIST CURRENT STATS
        System.out.println(player.getAllStats() + "\n\n\"Here's what I've got:\"");
        
        // ITEM PRICES
        powerChargePrice = 80;
        powerCellPrice = 30;
        rationsPrice = 5;
        shieldUpgradePrice = 450 * player.canBuyShield();
        oxygenPrice = (int) Math.round(Math.abs(player.getOxygen() - 100) * 2);
        powerChargePrice = (int) Math.round(Math.abs(player.getPower() - player.getPowerCapacity()) * 1.8); // calculated based on delta between current capacity and max

        startShopDialogue(input, player, oxygenPrice, powerCellPrice, rationsPrice, shieldUpgradePrice, powerChargePrice, 0);

        // initialize world variables
        timeElapsed = 0;    // resets timer count
        distanceTraveled = 0;
        eventCounter = 0;
        t.start();          // key listener thread


        // WORLD UPDATER
        while (distanceRemaining > 0){
            clearScreen();
            int warningFlag = 0;

            // Pitstop check
            // make sure not to overflow the buffer
            if (stopsCounter < pitStopsArray.length) {
                // check distance compared to pitstop distance, if you've passed it, stop at it and start the dialogue.
                if (distanceRemaining < pitStopsArray[stopsCounter]) {
                    System.out.println("You've just landed at a new spaceport. Time to stretch your legs and have a look around!\n");
                    // end KeyListener thread
                    System.out.println("\nPress ENTER to continue.");
                    while (keyPressed == false) {
                        // wait until key is pressed
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    clearScreen();
                    System.out.println(player.getAllStats());
                    System.out.println();
                    stopsCounter++;
                    startShopDialogue(input, player, oxygenPrice*(stopsCounter + 1), powerCellPrice*(stopsCounter + 1), rationsPrice*(stopsCounter + 1), shieldUpgradePrice*(stopsCounter + 1), powerChargePrice*(stopsCounter + 1), stopsCounter);

                    keyPressed = false;
                    resetKeyListener(userInputListener);
                }
            }

            // check the flag
            if (keyPressed == true) {
                // run menu subroutine
                menu(input, player);
                // resets flags and launches new KeyListener thread
                resetKeyListener(userInputListener);
            }

            // update every day until destinaiton is reached
            warningFlag = player.updateStatus();
            distanceRemaining -= player.getMilesPerDay();
            distanceTraveled += player.getMilesPerDay();
            int milesToStop = (distanceRemaining - pitStopsArray[stopsCounter]) < 0 ? 0 : (distanceRemaining - pitStopsArray[stopsCounter]);
            timeElapsed++;
            
            // Print Stats
            System.out.println(player.getAllStats() + "\n\nCurrent Speed: " + player.getMilesPerDay()/24 + " MPH");
            System.out.println("Miles to " + destinaiton + ": " + distanceRemaining + " (" + distanceRemaining/player.getMilesPerDay() +" Days)");
            System.out.println("Miles to next port: " + milesToStop);
            System.out.println("Days in space: " + timeElapsed);

            // Roll random number to determine if there will be an event
            if (timeElapsed > 3 && rng.nextInt(50) >= 43) {
                eventCounter++;
                // add the return to the warning flag
                warningFlag++;
                isEvent = true;
            }

            // check warning flag and pause update if something needs to be addressed
            if (warningFlag > 0) {

                if (isEvent) {
                    currentEvent = events.getRandomEvent(rng.nextInt(events.getNumberOfEvents()));
                    System.out.println("\nEVENT:\n" + currentEvent.getEventName());
                    System.out.println("Press ENTER to continue.");
                    
                    // end KeyListener thread
                    while (keyPressed == false) {
                        // wait until key is pressed
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    clearScreen();

                    // print event and update ship
                    player.parseEvent(currentEvent);

                    advanceText(input);

                    resetKeyListener(userInputListener);

                    // reset event flag when done
                    isEvent = false;

                    // start next day (MAY NOT USE THIS REALLY)
                    continue;
                } else {
                    System.out.println("\nWARNING ISSUED - ACKNOWLEDGEMENT REQUIRED\nPress ENTER to continue.");
                    // end KeyListener thread
                    while (keyPressed == false) {
                        // wait until key is pressed
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            System.out.println("\nPress ENTER to make operational changes for your ship.");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameWon(playerName, destinaiton, timeElapsed, player.getPower(), player.getCredits(), player.getRations(), eventCounter, distanceTraveled);
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
                System.out.println("\nYou engine speed is currently set to "+ player.getEngineSpeed());
                System.out.println("What speed would you like to set it to?\n(1) Off (+2 charge/day)\n(2) Low (-"+player.calcEngingPowerDraw(1)+" charge/day)\n(3) Medium (-"+ player.calcEngingPowerDraw(2) +" charge/day)\n(4) High (-"+ player.calcEngingPowerDraw(3)+" charge/day)\n(5) Turbo (-"+ player.calcEngingPowerDraw(4)+" charge/day)");
                choice2 = getChoiceNumber(input, 5);
                player.setEngineSpeed(choice2 -1); // EngineSpeed is 0 indexed in PlayerShip class
                clearScreen();
                // returns you to the menu until you intentionally exit (option 5)
                System.out.println("Engine speed set to " + player.getEngineSpeed() + "\n");
                menu(input, player);
                break;
            case 2:
                // MEALS
                System.out.println("\nYou are currently eating a "+ player.getMealPlan() + " meal plan");
                System.out.println("What plan would you like to set it to?\n(1) Meager\n(2) Lean\n(3) Regular");
                choice2 = getChoiceNumber(input, 3);
                player.setMealPlan(choice2);
                clearScreen();
                // returns you to the menu until you intentionally exit (option 5)
                System.out.println("Meal plan set to " + player.getMealPlan() +"\n");
                menu(input, player);
                break;
            case 3:
                // TODO SHIELD
                System.out.println("SHIELDS NOT IMPLEMENTED. I started this on monday afternoon, please cut me some slack.");
                // returns you to the menu until you intentionally exit (option 5)
                menu(input, player);
                break;
            case 4:
                System.out.println("\nHELP:\n\nThe engine uses power cells in order to run. The it takes exponentially more power to run as speed increases, so it's best only use it when necessary. Your ship has solar power, and can slowly recharge if the engine is left off. Since you are in space, you will retain some forward momentum even when the engines are off.\n\nYour meal plan determines how quickly you will go through your rations. The less you eat however, the more often you may get sick, so keep that in mind.\n\nThe shields are on by default, and protect against small amounts of space debris and other minor damage. However the shield draws power too and can be disabled to help offset battery draining.");
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
        return;
    }

    private static void printShopPrices(int oxygenPrice, int powerCellPrice, int rationsPrice, int shieldUpgradePrice, int powerChargePrice) {
        System.out.println("(1) Oxygen refills - " +oxygenPrice + " Credits to completely fill");
        System.out.println("(2) Power Cells (increase range) - " + powerCellPrice + " Credits each");
        System.out.println("(3) Rations - " + rationsPrice + " Credits per pound");
        System.out.println("(4) Upgraded Shields - " + shieldUpgradePrice + " Credits");
        System.out.println("(5) Charge power cells - "+ powerChargePrice +" Credits to fully charge");
        System.out.println("(6) Leave the shop");
        return;
    }

    private static void startShopDialogue(Scanner input, PlayerShip player, int oxygenPrice, int powerCellPrice, int rationsPrice, int shieldUpgradePrice, int powerChargePrice, int stopCounter){
        // PRINT ITEM LIST
        oxygenPrice = (int) Math.round(Math.abs(player.getOxygen() - 100) * 2 * (2.2 + stopCounter));
        powerChargePrice = (int) Math.round(Math.abs(player.getPower() - player.getPowerCapacity()) * 1.8 * (stopCounter + 0.5)); // calculated based on delta between current capacity and max

        printShopPrices(oxygenPrice, powerCellPrice, rationsPrice, shieldUpgradePrice, powerChargePrice);
        int numberOfOptions = 6;
        int choice = getChoiceNumber(input, numberOfOptions);

        while (choice != 6) {
            clearScreen();
            switch (choice) {
                case 1:
                    System.out.println("Credits remaining: " + player.getCredits());
                    System.out.print("\nDo you want to refill your O2 tanks? You've got " + player.getOxygen() + "% left in the tank. It'll cost ya "+ oxygenPrice +"\n(Enter YES or NO): ");
                    player.buyItems("oxygen", getYesNo(input), oxygenPrice);
                    break;
                case 2:
                    System.out.println("Credits remaining: " + player.getCredits());
                    System.out.print("Current stock of power cells: "+player.getPowerCapacity()+"\n\nHow many power cells do ya want? - " + powerCellPrice + " Credits per cell (HINT: you can sell your own stock using negative numbers)\n(Type number and hit ENTER): ");
                    player.buyItems("power", input.nextInt(), powerCellPrice);
                    break;
                case 3:
                    System.out.println("Credits remaining: " + player.getCredits());
                    System.out.print("Current stock of rations: "+player.getRations()+" pounds\n\nHow many pounds of food do ya want? " + rationsPrice + " Credits per pound (HINT: you can sell your own stock using negative numbers)\n(Typer number and hit ENTER): ");
                    player.buyItems("rations", input.nextInt(), rationsPrice);
                    break;
                case 4:
                    if (player.canBuyShield() == 0) {
                        System.out.println("You already upgraded your shields, remember?\n");
                        break;
                    }
                    System.out.println("Credits remaining: " + player.getCredits());
                    System.out.print("\nSo ya wanna upgrade that weak shield huh? That'll be "+ shieldUpgradePrice +" Credits. Remember, there are no refunds on this one!\n(Enter YES or NO): ");
                    player.buyItems("shields", getYesNo(input), shieldUpgradePrice);
                    break;
                case 5:
                    System.out.println("Credits remaining: " + player.getCredits());
                    System.out.print("\nI can charge those batteries up for ya if you want. You've currently got a charge of " + player.getBatteryPercentage() + "%.\nThat'll cost ya " + powerChargePrice + " Credits. What do ya say?\n(Enter YES or NO): ");
                    player.buyItems("charge", getYesNo(input), powerChargePrice);
                    break;
                case 6:
                    System.out.println("Alright, see ya around kid!");
                    return;
                default:
                    System.out.println("We don't got that here.");
                    break;
            }
            System.out.println(player.getAllStats());
            System.out.println("\nAnything else ya need?");
            
            // Update variable prices
            oxygenPrice = (int) Math.round(Math.abs(player.getOxygen() - 100) * 2);
            powerChargePrice = (int) Math.round(Math.abs(player.getPower() - player.getPowerCapacity()) * 1.8); // calculated based on delta between current capacity and max
            shieldUpgradePrice = 650 * player.canBuyShield();
            printShopPrices(oxygenPrice, powerCellPrice, rationsPrice, shieldUpgradePrice, powerChargePrice);
            choice = getChoiceNumber(input, numberOfOptions);
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

    private static void gameWon(String name, String destination, int timeElapsed, int power, int credits, int rations, int eventsCount, int distanceTraveled){
        clearScreen();
        int score = (int) Math.round(((timeElapsed*20) + (credits*8 )+ (power *22) +  (rations*10) + (eventsCount*1500) + (distanceTraveled*0.00009))*0.2);
        System.out.println("You did it! you beat the game!\n FINAL SCORE: " + score + " points.\nCongratulations Captain " + name);
        switch (destination) {
            case "Interplanetary Base 8675-B":
                System.out.println("You've done the space equivalent of sailing Los Angeles, CA to Honolulu, HI.");
            case "Mars":
                System.out.println("You've done the space equivalent of traveling from Phoenix, AZ to New York City, NY on foot for 8 hours a day!.");
                break;
            case "Ganymede":
                System.out.println("You've done the space equivalent of traveling from the bottom of South America to the top on foot for 8 hours a day.");
                break;
            case "Titan":
                System.out.println("You've roughly done the space equivalent of walking across the entire globe on foot without taking a break!");
                break;
            default:
                break;
        }
        System.out.println("\nGame Finished");
    }
}
