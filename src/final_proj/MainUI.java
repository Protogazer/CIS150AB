package final_proj;

import java.util.Scanner;

public class MainUI {
    public static void main(String[] args) {
        // TODO include calendar system?
        // TODO make class for user inputs that encapsulates all methods?
        Scanner input = new Scanner(System.in);
        String playerName = "";
        String shipName = "";
        String destinaiton = "";

        int choiceNumber = -1;
        int numberOfOptions = -1;
        int milesToDestinaiton = -1;

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
                milesToDestinaiton = 75000000;
                destinaiton = "Interplanetary Base 8675-B";
                break;
            case 2:
                milesToDestinaiton = 140000000;
                destinaiton = "Mars";
                break;
            case 3:
                milesToDestinaiton = 390000000;
                destinaiton = "Ganymede";
                break;
            case 4:
                milesToDestinaiton = 746000000;
                destinaiton = "Titan";
                break;
            default:
            // default to Mars
                milesToDestinaiton = 140000000;
                destinaiton = "Mars";
                break;
        }

        // Create a separate thread for updating the world (days, distance, events)
        // executing this in its own thread allows for interruption and continuaiton
        // reference: https://stackoverflow.com/questions/19489054/break-continuous-loop-with-key
        WorldObject gameWorld = new WorldObject(milesToDestinaiton, playerName, shipName, destinaiton);
        Thread worldUpdateThread = new Thread(gameWorld);

        // PlayerShip player = new PlayerShip(playerName, shipName, milesToDestinaiton);
        clearScreen();

        // START GREETING
        System.out.println("Welcome captain " + playerName +"! You've finally gone and done it, you've bought your own starship named " + shipName + ". You've charted a course for " + destinaiton + " with a distance of " + milesToDestinaiton + " miles.\nBuying the ship has left you with only 1000 credits to your name and you haven't even bought your supplies yet! You head to Bright Side -the largest pitstop this side of the moon- in order to get supplies.\n");

        advanceText(input);

        System.out.println("As you enter Bright Side, a frail hunched figure, more wrinkles than man, slowly shuffles to greet you.\n\nShopkeep:\n\"Howdy! *cough* I see you've got a new ship. So they finally threw that hunk of junk away did they? Anyhow, if your here that means you need supplies. I think I've got just the ticket.\"\n");

        System.out.println("\nPress ENTER to continue.");
        input.nextLine();
        clearScreen();

        // SHOP INTERACTION CODE BELOW
        // LIST CURRENT STATS
        System.out.println(gameWorld.getAllStats() + "\n\n\"Here's what I've got:\"");
        
        // ITEM PRICES
        powerChargePrice = 50;
        powerCellPrice = 35;
        rationsPrice = 3;
        shieldUpgradePrice = 650;
        oxygenPrice = (int) Math.round(Math.abs(gameWorld.getOxygen() - 100) * 2);
        powerChargePrice = (int) Math.round(Math.abs(gameWorld.getPower() - gameWorld.getPowerCapacity()) * 1.8); // calculated based on delta between current capacity and max

        // PRINT ITEM LIST
        System.out.println("(1) Oxygen refills - " + oxygenPrice + " Credits to completely fill\n(2) Power Cells (increase range) - " + powerCellPrice + " Credits each\n(3) Rations - " + rationsPrice + " Credits per pound\n(4) Upgraded Shields - " + shieldUpgradePrice + " Credits\n(5) Charge power cells - "+ powerChargePrice +" Credits to fully charge\n(6) Leave the shop");
        numberOfOptions = 6;
        choiceNumber = getChoiceNumber(input, numberOfOptions);

        while (choiceNumber != 6) {
            clearScreen();
            switch (choiceNumber) {
                case 1:
                    System.out.print("Do you want to refill your O2 tanks? You've got " + gameWorld.getOxygen() + "% left in the tank. It'll cost ya "+ oxygenPrice +"\n(Enter YES or NO): ");
                    gameWorld.buyItems("oxygen", getYesNo(input), oxygenPrice);
                    break;
                case 2:
                    System.out.print("How many power cells do ya want? - " + powerCellPrice + " Credits per cell\n(Type number and hit ENTER): ");
                    gameWorld.buyItems("power", input.nextInt(), powerCellPrice);
                    break;
                case 3:
                    System.out.print("How many pounds of food do ya want? " + rationsPrice + "Credits per pound\n(Typer number and hit ENTER): ");
                    gameWorld.buyItems("rations", input.nextInt(), rationsPrice);
                    break;
                case 4:
                    System.out.print("So ya wanna upgrade that weak shield huh? That'll be "+ shieldUpgradePrice +"(Enter YES or NO): ");
                    gameWorld.buyItems("shields", getYesNo(input), shieldUpgradePrice);
                    break;
                case 5:
                    System.out.print("I can charge those batteries up for ya if you want. You've currently got a charge of " + gameWorld.getBatteryPercentage() + "%.\nThat'll cost ya " + powerChargePrice + " Credits. What do ya say? (Enter YES or NO): ");
                    gameWorld.buyItems("charge", getYesNo(input), powerChargePrice);
                break;
                case 6:
                    System.out.println("Alright, see ya around kid!");
                default:
                    System.out.println("We don't got that here.");
                    break;
            }
            System.out.println(gameWorld.getAllStats());
            System.out.println("\nAnything else ya need?");
            
            // Update variable prices
            oxygenPrice = (int) Math.round(Math.abs(gameWorld.getOxygen() - 100) * 2);
            powerChargePrice = (int) Math.round(Math.abs(gameWorld.getPower() - gameWorld.getPowerCapacity()) * 1.8); // calculated based on delta between current capacity and max

            System.out.println("(1) Oxygen refills - " + oxygenPrice + " Credits to completely fill\n(2) Power Cells (increase range) - " + powerCellPrice + " Credits each\n(3) Rations - " + rationsPrice + " Credits per pound\n(4) Upgraded Shields - " + shieldUpgradePrice + " Credits\n(5) Charge power cells - "+ powerChargePrice +" Credits to fully charge\n(6) Leave the shop");
            choiceNumber = getChoiceNumber(input, numberOfOptions);
        }

        // start separate thread for world updates
        worldUpdateThread.start();

        // listen to the keyboard to interrupt the world updates
        pollKeyboard(input, gameWorld);

    }

    private static void advanceText(Scanner input) {
        System.out.println("\nPress ENTER to continue.");
        input.nextLine();
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

    private static void pollKeyboard(Scanner input, WorldObject gameWorld) {
        boolean refreshKeyboard = true;
        while (refreshKeyboard && gameWorld.getEventStarted() == false) {
            String userInput = input.nextLine();
            if (userInput.isEmpty() || !userInput.isEmpty()) { // stops counter even if you accidentally entered a character before hitting ENTER
                gameWorld.stopCounter();
                refreshKeyboard = false;
                System.out.println("\nYou stop for a minute and think... What do you want to do right now?");
                break;
            }
        }
    }

    private static void clearScreen(){
        System.out.print("\033\143"); // clears the terminal in vscode (according to https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java)
    }
}
