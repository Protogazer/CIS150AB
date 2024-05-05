package final_proj;

// this is used to create a separate thread for execution of the actual game world and ship information
// all data about the world and ship will be handled here, while user input and choices will be handled in the main.java file
// neccessary methods for the world and ship will be exposed for use in the main.java
public class WorldObject {
    
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
}

