package final_proj;

// this is used to create a separate thread for execution of the actual game world and ship information
// all data about the world and ship will be handled here, while user input and choices will be handled in the main.java file
// neccessary methods for the world and ship will be exposed for use in the main.java
public class WorldObject implements Runnable {
    
    String playerName = "";
    String shipName = "";
    String destinaitonName = "";
    
    // create empty player ship
    PlayerShip player;

    int distance = -1;
    int timeElapsed = -1;
    int shipSpeed = -1;

    // update flags
    boolean runUpdateWorld = false;
    boolean destinationReached = false;
    boolean eventStarted = false;
    
    public WorldObject(int distance, String playerName, String shipName, String destination) {
        this.distance = distance;
        this.destinaitonName = destination;
        timeElapsed = 0;
        runUpdateWorld = false;
        destinationReached = false;
        player = new PlayerShip(playerName, shipName, distance);
    }
    
    // SHIP SETTERS

    // SHIP GETTERS
    public String getAllStats() {
        return player.getAllStats();
    }
    public int getCredits() {
        return player.getCredits();
    }
    public int getOxygen() {
        return player.getOxygen();
    }
    public int getPower() {
        return player.getPower();
    }
    public int getPowerCapacity(){
        return player.getPowerCapacity();
    }
    public int getRations() {
        return player.getRations();
    }
    public int getPowerUsage() {
        return player.getPowerUsage();
    }
    public int getMilesPerDay() {
        return player.getMilesPerDay();
    }
    public float getBatteryPercentage() {
        return player.getBatteryPercentage();
    }

    // OTHER SHIP METHODS
    public int buyItems(String item, int qty, int unitPrice) {
        return player.buyItems(item, qty, unitPrice);
    }

    // WORLD GETTERS
    public int getTimeElapsed() {
        return timeElapsed;
    }
    public int getDistance() {
        return distance;
    }
    public void setShipSpeed(int speed) {
        this.shipSpeed = speed;
    }
    public boolean getEventStarted() {
        return eventStarted;
    }


    // starts the counter if the destination has not been reached
    public void runCounter() {
        if (!destinationReached) {
            runUpdateWorld = true;
        } else {
            runUpdateWorld = false;
            System.out.println("Destination reached, congratulations!");
        }
    }

    // stops the counter
    public void stopCounter() {
        runUpdateWorld = false;
    }


    @Override
    public void run() {
        runUpdateWorld = true;

        // while the count has not finished,
        while(destinationReached == false) {
            // check the keepRunning bool
            if (runUpdateWorld) {
                updateWorld();
            }

            // slows retry
            // System.out.println("Keep running flag set false! Rechecking in 2s");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // runs the main world update, including day increment and distance calculations
    private void updateWorld() {
        while (distance > 0 && runUpdateWorld == true){
            clearScreen();
            System.out.println(player.getAllStats() + "\n\nCurrent Speed: " + player.getMilesPerDay()/24 + " MPH");
            // ROLL FOR EVENT
                // if event is rolled 
                    //runUpdateWorld = false;
                    //eventStarted = true;
                    //return;
            
            System.out.println("\nAll Clear.\n");
            // update every day until destinaiton is reached
            distance -= player.getMilesPerDay();
            player.updateStatus();
            timeElapsed++;
            System.out.println("Miles to " + destinaitonName + ": " + distance);
            System.out.println("Days in space: " + timeElapsed);
            System.out.println("\nPress ENTER to stop and consider your situation.");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (distance <= 0) {
            System.out.println("COUNT COMPLETED");
            destinationReached = true;
        }
    }

    private void clearScreen(){
        System.out.print("\033\143"); // clears the terminal in vscode (according to https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java)
    }
}

