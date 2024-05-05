package final_proj;


/*
 * manages all random events for the game
 * This is separate from MainGame for readability.
 * 
 * Important methods: getRandomEvent()
 */

public class EventManager {
    
    private RandomEvent eventArray[];
    private int numberOfEvents = 20;

    public EventManager() {
        initializeEvents();
    }


    public int getNumberOfEvents() {
        return numberOfEvents;
    }
    public RandomEvent getRandomEvent(int randomNumber) {
        return eventArray[randomNumber];
    }


    private void initializeEvents() {
        eventArray = new RandomEvent[numberOfEvents];

        eventArray[0] = new RandomEvent(
            "Asteroid Field",
            "You accidentally drift into an asteroid field. Your ship takes damage. While you try to patch things up, you lose some oxygen.",
            0,
            -5,
            -3,
            0
        );

        eventArray[1] = new RandomEvent(
            "Scavengers pursue the ship!",
            "Over the radio, they try to cut a deal with you. 150 Credits and some food seems like a small price to pay for your life.",
            -150,
            0,
            0,
            -20
        );
        eventArray[2] = new RandomEvent(
            // TODO CHOICE
            "Distress Signal",
            "The Good Kind",
            250,
            0,
            0,
            -10
        );
        eventArray[3] = new RandomEvent(
            // TODO CHOICE
            "Distress Signal",
            "The bad kind",
            -200,
            0,
            -20,
            -10
        );
        eventArray[4] = new RandomEvent(
            "Unidentified object spotted!",
            "What the hell is that thing?? You see a bright flash. In an instant the object is gone and your ship makes a whirring sound.",
            0,
            50,
            50,
            0
        );
        eventArray[5] = new RandomEvent(
            "Solar Wind is picking up",
            "There's a good gust of solar wind. You gain some energy by turning off your thrusters and letting the wind push you for a while",
            0,
            0,
            10,
            0
        );
        eventArray[6] = new RandomEvent(
            "Solar Flare",
            "Your systems take damage from the solar flare! Five of your power cells go offline in the process",
            0,
            0,
            -5,
            0
        );
        eventArray[7] = new RandomEvent(
            "Sparks in oxygen storage!",
            "Due to faulty equipment in the oxygen storage, you've lost some of your O2. Maybe it's time to practice holding your breath?",
            0,
            -10,
            0,
            0
        );
        eventArray[8] = new RandomEvent(
            // I WANT THIS TO BE A CHOICE
            "Raging Robots!",
            "You spot some movement on a nearby asteroid. Upon closer inspection, it seems that a crew of androids has taken the asteroid as their own and are hailing you. They demaind payment to cross through their territory or they'll open fire. You disagree. You destroy them and scavenge their parts.",
            0,
            0,
            35,
            0
        );
        eventArray[9] = new RandomEvent(
            "AI Revolt",
            "Your AI computer has determined that you are a threat to the mission. In an attempt to kill you, it has purged oxygen from the ship. You were able to reprogram it before you lost consciousness.",
            0,
            -12,
            0,
            0
        );
        eventArray[10] = new RandomEvent(
            "Unknown lifeform found aboard ship",
            "You see a small grey mass from the corner of your eye. You don't know what it is, but you know that it's not supposed to be here! After some trouble, you corner it in the airlock and blast it outside",
            0,
            -1,
            0,
            0
        );
        eventArray[11] = new RandomEvent(
            "Space Lottery",
            "JACKPOT! You knew your luck would start to turn eventually. That ticket you bought before leaving Earth was a winner after all. You check your bank account and find a nice sum has been added.",
            500,
            0,
            0,
            0
        );
        eventArray[12] = new RandomEvent(
            "Leaky Pipes",
            "Even in space, pipes still leak. Some of the water meant for your garden has been lost for good. Looks like you'll have a pretty poor harvest this year.",
            0,
            0,
            0,
            -8
        );
        eventArray[13] = new RandomEvent(
            "Restaraunt at the End of the Universe",
            "Although the name's not quite accurate, you're glad to find some good food out here in the middle (edge?) of space.",
            -45,
            15,
            0,
            30
        );
        eventArray[14] = new RandomEvent(
            // TODO CHOICE
            "Sketchy delivery",
            "You've been hailed by an unknown frequency. They're offering you 300 Credits to deliver an unmarked package when you land at your final destination, no questions asked. They'll make it worth your while.",
            250,
            0,
            0,
            0
        );
        eventArray[15] = new RandomEvent(
            "Rewiring the ship",
            "With all this spare time on your hands, you take it upon yourself to rewire the ship's power circuit. In the process you're able to make your ship a bit more efficient.",
            0,
            0,
            20,
            0
        );
        eventArray[16] = new RandomEvent(
            "",
            "Description",
            0,
            30,
            0,
            0
        );
        eventArray[17] = new RandomEvent(
            // TODO CHOICE
            "A robot salesman has appeared",
            "You recieve a notification that your ship is being boarded! You rush over to the dock and brace for a fight, but are instead met with a rather genial robotic man. He beeps, he boops, and he makes deals. He's willing you charge your ship for a small fee.",
            -50,
            0,
            35,
            0
        );
        eventArray[18] = new RandomEvent(
            // TODO CHOICE
            "Wreckage spotted",
            "You hear a light clank echo through the ship's hull. It seems you've hit the wreckage of some other poor fool of a pilot. Your scanner doesn't pick up anyone nearby, so you decide to take a look around for supplies.",
            0,
            25,
            25,
            20
        );
        eventArray[19] = new RandomEvent(
            "Vending machine spotted!",
            "Looking out your window, you spot a vending machine sitting alone on a small asteroid. You decide to go over and see what it's got. Sadly, it only had an egg salad sandwich and a pack of AA batteries.",
            -10,
            0,
            3,
            2
        );
        /* Other ideas/ ideas that need new mechanics:
         * Internet Gambling
         * Frozen oxygen comet
         * the ship is frozen, needs to thaw out
         * Somethings been growing in the fridge
         * something to do with time or locaiton
         * bad aliens
         */
    }

}
