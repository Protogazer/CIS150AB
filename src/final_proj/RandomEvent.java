package final_proj;

public class RandomEvent {
    private String eventName = "";
	private String eventDescription = "";
	// private int timeElapsed = 0;
	private int credits = 0;
	private int power = 0;
	private int oxygen = 0;
	private int rations = 0;
    private static int numberOfStats = 4;
    private boolean isEvent = false;

    public RandomEvent(String eventName, String eventDescription, int credits, int oxygen, int power, int rations){
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        // this.timeElapsed = timeElapsed;
        this.credits = credits;
        this.oxygen = oxygen;
        this.power = power;
        this.rations = rations;
    }

    public boolean getIsEvent() {
        return isEvent;
    }

    // returns this event as is, then parse the stats in the updater
    public RandomEvent rollEvent(){
        System.out.println(eventName);
        System.out.println(eventDescription);

        System.out.println("\nPress ENTER to continue.");

        return this;
    }
}
