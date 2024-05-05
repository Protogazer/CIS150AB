package final_proj;

/*
 * Alex Johnson
 * Final Project RandomEvent class
 * 
 * A class used to hold the data of a random event. Includes the name, description and status effects
 * for a random event. This is used in conjunction with EventManager.java to create an array of
 * RandomEvents to make it easier to create and roll for a random event at runtime.
 */

public class RandomEvent {
    private String eventName = "";
	private String eventDescription = "";
	// private int timeElapsed = 0;
	private int creditsChange = 0;
	private int powerChange = 0;
	private int oxygenChange = 0;
	private int rationsChange = 0;
    // private static int numberOfStats = 4;

    public RandomEvent(String eventName, String eventDescription, int credits, int oxygen, int power, int rations){
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        // this.timeElapsed = timeElapsed;
        this.creditsChange = credits;
        this.oxygenChange = oxygen;
        this.powerChange = power;
        this.rationsChange = rations;
    }

    public String getEventName() {
        return eventName;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public int getCreditsChange() {
        return creditsChange;
    }
    public int getOxygenChange() {
        return oxygenChange;
    }
    public int getPowerChange() {
        return powerChange;
    }
    public int getRationsChange() {
        return rationsChange;
    }
}
