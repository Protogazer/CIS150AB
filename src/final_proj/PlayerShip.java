package final_proj;

public class PlayerShip {
    private String playerName = "";
    private String shipName = "";

    // stats the user can see and effect
    private int credits = 0;    // whatever
    private int oxygen = 0;     // percentage
    private int power = 0;      // percentage
    private int powerCapacity = 0;   // power cells
    private int rations = 0;    // pounds
    // private String[] crewMembers = {}; maybe add this, don't know what for yet

    // factors that determine stat usage. USAGE IS POSITIVE, AND IS SUBTRACTED FROM TOTAL UPON UPDATE
    private boolean shieldsOn = false;
    private boolean shieldsUpgrade = false;
    private int engineThrust = 0;   // 0-4 (off, low, medium, high, turbo)
    private int milesPerDay = 0;    // set by engine thrust
    private int powerUsage = 0;     // determined by shield status and entine thrust
    private int oxygenConsumption = 1;
    private int rationUsage = 2;
    private int daysWithoutFood = 0;

    // player gets to choose name and how long they have to travel which affects
    // rations
    public PlayerShip(String name, String shipName, int gameLength) {
        this.playerName = name;
        this.shipName = shipName;
        this.credits = 1000;
        this.oxygen = 30;
        this.power = 95;
        this.powerCapacity = 100;
        this.rations = (int) Math.round((gameLength * 0.0000008));
        setEngineSpeed(1);
    }

    // GETTERS
    public float getBatteryPercentage() {
        return ((float) power/powerCapacity * 100);
    }
    public int getCredits() {
        return credits;
    }
    public String getEngineThrust() {
        String speed;
        switch (engineThrust) {
            case 0:
                speed = "Off";
                break;
            case 1:
                speed = "Low";
                break;
            case 2:
                speed = "Medium";
                break;
            case 3:
                speed = "High";
                break;
            default:
                speed = "ERROR";
                break;
        }
        return speed;
    }
    public int getEnginePowerDraw(int engineSpeedLevel) {
        return Math.round(engineSpeedLevel * (float) 2.3);
    }
    public int getMilesPerDay() {
        return milesPerDay;
    }
    public int getOxygen() {
        return oxygen;
    }
    public String getPlayerName() {
        return playerName;
    }
    public int getPower() {
        return power;
    }
    public int getPowerCapacity() {
        return powerCapacity;
    }
    public int getPowerUsage() {
        return powerUsage;
    }
    public int getRations() {
        return rations;
    }
    public String getShipName() {
        return shipName;
    }
    // lists current levels for oxygen, power, credits and rations
    public String getAllStats() {
        return ("STATS FOR " + shipName.toUpperCase() + ":\nOxygen remaining: " + oxygen + "%\nPower remaining: "
                + getBatteryPercentage() + "% ("+ power +"/"+ powerCapacity +" cells used)\nRations remaining: " + rations + " pounds\nCredits remaining: " + credits);
    }

    // SETTERS
    public void setEngineSpeed(int speed) {
        if (speed >= 0 && speed <= 4) {
            engineThrust = speed;

            switch (engineThrust) {
                case 0:
                    this.milesPerDay = 532600;
                    break;
                case 1:
                    this.milesPerDay = 2066400;
                    break;
                case 2:
                    this.milesPerDay = 4132800;
                    break;
                case 3:
                    this.milesPerDay = 6200000;
                    break;
                case 4:
                    this.milesPerDay = 9820000;
                default:
                    break;
            }
            updatePowerUsage();
        } else {
            System.out.println("Unexpected option encountered, speed remains unchanged.");
        }
    }

    public void setOxygenConsumption(int extraO2Usage) {
        // TODO change this when damaged or more crew members
        this.oxygenConsumption = 1 + extraO2Usage;
    }

    public void toggleShields() {
        shieldsOn = (shieldsOn == true) ? false : true;
    }

    // sets power usage based on if shields are active,
    //and how high the engine thrust is
    private void updatePowerUsage() {
        int defaultPowerDraw = 0;

        if (engineThrust == 0) {
            // Engine is off, slowly recharge from solar (2 cells per day) 
            // TODO ADD SOLAR UPGRADE;
            defaultPowerDraw -= 2;
        }
        // add shield power draw
        if (this.shieldsOn) {
            defaultPowerDraw++;
        }
        // add engine power draw (0*2 is still 0)
        defaultPowerDraw += getEnginePowerDraw(engineThrust);
        this.powerUsage = defaultPowerDraw;
    }

    // SHOP ITEMS BELOW
    public int buyItems(String item, int qty, int unitPrice) {
        String shopResponse = "";

        if ((qty * unitPrice) > this.credits) {
            System.out.println("Who are you trying to fool? You don't have that kind of money!\n");
            return -1;
        }

        if (item.equals("oxygen")) {
            if (qty == 1) {
                oxygen = 100;
                shopResponse = "your O2 tank has been filled to the brim. ";
            } else {
                shopResponse = "Alright, I hope you can hold your breath then. ";
                return 0;
            }
        } else if (item.equals("charge")){
            if (qty == 1) {
                power = powerCapacity;
                shopResponse = "You're batteries all fully charged and ready to go! ";
            } else {
                shopResponse = "Alright, I hope you can hold your breath then. ";
                return 0;
            }
        } else if (item.equals("shields")) {
            if (qty == 1) {
                if (shieldsUpgrade == true) {
                    System.out.println("You've already upgraded your shields. They're already top of the line.\n");
                    return 0;
                }
                shieldsUpgrade = true;
                shopResponse = "I activated the \"Rocks and Raiders\" package on your shields. ";
            } else {
                shopResponse = "Hope you're a good pilot! ";
                return 0;
            }
        } else if (qty == 0) {
            System.out.println("Okay, no problem. Your money's probably no good anyway...\n");
            return 1;
        } else {
            switch (item) {
                case "power":
                    powerCapacity += qty;
                    shopResponse = (qty + " power cells. ");
                    break;
                case "rations":
                    rations += qty;
                    shopResponse = (qty + " pounds of rations. They're... edible. ");
                    break;
                default:
                    System.err.println("Unknown shop item! Aborting purchase");
                    return -1;
            }
        }

        this.credits -= (qty * unitPrice);
        System.out.println("\nHere ya go, " + shopResponse + "Thanks for your busniness!\n");
        // System.out.println("Credits remaining:" + credits + "\n");
        return 0;
    }

    // updates status for power, oxygen and rations when called
    public int updateStatus() {
        int warningCount = 0;

        if (power > 0 || power < powerCapacity) {
            power = Math.clamp((power - powerUsage), 0, powerCapacity);

            // WARN IF LOW
            if (power > 0 && ((float)power / powerCapacity) <= 0.1) {
                System.out.println("\nWARNING: POWER REMAINING IS BELOW 10. ADJUST POWER INTAKE IMMEDIATELY\n");
            }

            // check for 0
            if (power == 0) { 
                // TODO keep subtracting oxygen and rations
                System.out.print("\033\143");
                System.out.println("\nYou are out of power! You are left drifting through space.\nYour thrusters have been turned off in hopes of recharging the batteries.\nAll you can do now is cross your fingers and hope you hold out until you make it to a base or help arrives...\n");
                setEngineSpeed(0); // set engines to off

                warningCount++;

                // turn shields off
                shieldsOn = false;
            }
        }
        if (oxygen > 0 || oxygen < 100) {
            oxygen = Math.clamp((oxygen - oxygenConsumption), 0, 100);
            // check for 0
            if (oxygen == 0) { 
                System.out.print("\033\143");
                System.out.println("Well, that's no good. You're out of oxygen!\n\nIn your last moments you remember something- a popular traveler's guide. It stated that \"if you hold a lungful of air you can survive in the total vacuum of space for about thirty seconds. However, it does go on to say that what with space being the mindboggling size it is the chances of getting picked up by another ship within those thirty seconds are two to the power of two hundred and seventy-six thousand seven hundred and nine to one against\". You did not beat those odds.\n\nGAME OVER");
                System.exit(0);
            }
        }
        if (rations > 0) {
            rations = Math.clamp((rations - rationUsage), 0, 99999);
            if (rations == 0) {
                // TODO allow for 14 more days to pass, increase illness
                rations = 0;
                System.out.println("Your stomache is in pain, and you're getting weak. You're not sure how long you can really survive without food!");
                if (daysWithoutFood == 0) {
                    warningCount++;
                }
                daysWithoutFood++;

                // TODO maybe randomize this between 14-31 the first time rations goes to 0 ?
                if (daysWithoutFood >= 26) {
                    System.out.print("\033\143");
                    System.out.println("You've held out for as long as you could, but your strength has failed you. You can't think, you can't speak, you can't move from where you lie on "+ shipName +"\'s cold floor.\n\nGAME OVER");
                    System.exit(0);
                }
            }
        }
        return warningCount;
    }
}
