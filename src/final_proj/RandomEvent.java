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

    public RandomEvent(String eventName, String eventDescription, int credits, int oxygen, int power, int rations){
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        // this.timeElapsed = timeElapsed;
        this.credits = credits;
        this.oxygen = oxygen;
        this.power = power;
        this.rations = rations;
    }

    // returns the all stats as an array. This order MUST be maintained when processing these stats in the PlayerShip class (alphabetical order)
    public int[] rollEvent(){
        System.out.println(eventName);
        System.out.println(eventDescription);

        int[] stats = {credits,oxygen,power,rations};

        // print the outcome of the event
        for (int i = 0; i < numberOfStats; i++) {
            String currentStat = "";
            switch (i) {
                case 0:
                    currentStat = "credits";
                    break;
                case 1:
                    currentStat = "oxygen";
                    break;
                case 2:
                    currentStat = "Power";
                    break;
                case 3:
                    currentStat = "Rations";
                    break;
                default:
                    currentStat = "ERROR";
                    break;
            }
            if (stats[i] > 0) {
                System.out.println("+" + stats[i] + currentStat);
            } else if (stats[i] > 0) {
                System.out.println("-" + stats[i] + " " + currentStat);
            }
        }
        System.out.println("\nPress ENTER to continue.");

        return stats;
    }
}
