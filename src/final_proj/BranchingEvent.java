package final_proj;

public class BranchingEvent extends RandomEvent{
    // Alternative branch info
    private String eventBad = "The ship took a few hits during the commotion, and some oxygen was lost before all the holes could be repaired.";
	private String eventGood = "You narrowly dodged an oncoming meteor shower, and were able to collect a few of the in the process. They're rich in minerals and rare metals!";
	private String statuEffectBad = "-2 Oxygen\n-1 Shields";
	private String statusEffectGood = "+100 Credits";
    
    public BranchingEvent(String eventName, String eventDescription, int timeChange, int powerChange, int rationChange, int oxygenChange, int creditChange){
        super(eventName, eventDescription, timeChange, powerChange, rationChange, oxygenChange, creditChange);
    }
    
    private void rollEvent(int userChoice){
        // get user choice, and roll odds with that in mind, return good or bad response based based on roll
        // odds are better depending on the choice, but this is obfuscated for the player
    }
}
