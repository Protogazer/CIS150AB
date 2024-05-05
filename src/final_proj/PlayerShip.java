package final_proj;

/*
 * Alex Johnson
 * Final Project PlayerShip class
 * 
 * A class to manage the player's ship and all changes to it's stats that occur during gameplay
 * 
 * The player/ship are created as a separate PlayerShip object, storing all information
 * about the health and stats of the player. Some elements were commented out since they
 * were not implemented in time.
 */

import java.util.Random;

public class PlayerShip {
    private String playerName = "";
    private String shipName = "";

    // stats the user can see and effect
    private int credits = 0;        // whatever
    private int oxygen = 0;         // percentage
    private int power = 0;          // percentage
    private int powerCapacity = 0;  // power cells
    private int rations = 0;        // pounds
    // private String[] crewMembers = {}; maybe add this, don't know what for yet


    // factors that determine stat usage. USAGE IS POSITIVE, AND IS SUBTRACTED FROM TOTAL UPON UPDATE
    private boolean shieldsOn = false;
    private boolean canBuyShield = true;    // only used when setting shield price, starts true
    
    private String engineSpeed = "Off";
    private String mealPlan = "Regular";

    private int engineThrust = 0;           // 0-4 (off, low, medium, high, turbo)
    private int milesPerDay = 0;            // set by engine thrust
    private int powerUsage = 0;             // determined by shield status and entine thrust
    private int oxygenConsumption = 1;
    private int rationUsage = 4;
    private int daysWithoutFood = 0;


    // player gets to choose name and how long they have to travel which affects rations
    public PlayerShip(String name, String shipName, int gameLength) {
        this.playerName = name;
        this.shipName = shipName;
        this.credits = 750;
        this.oxygen = 100;
        this.power = 150;
        this.powerCapacity = 150;
        this.rations = (int) Math.round((gameLength * 0.0000009));
        setEngineSpeed(1);
    }

    // GETTERS
    public float getBatteryPercentage() {
        return ((float) power/powerCapacity * 100);
    }
    public int getCredits() {
        return credits;
    }
    public String getEngineSpeed() {
        return engineSpeed;
    }
    public int calcEngingPowerDraw(int engineSpeedLevel) {
        return Math.round((engineSpeedLevel + 1) * engineSpeedLevel);
    }
    public String getMealPlan() {
        return mealPlan;
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
                + getBatteryPercentage() + "% ("+ power +"/"+ powerCapacity +" cells charged)\nRations remaining: " + rations + " pounds\nCredits remaining: " + credits);
    }
    public int canBuyShield(){
        return canBuyShield == true ? 1 : 0; // returns 1 if yes, 0 if no
    }

    // SETTERS
    public void setEngineSpeed(int speed) {
        if (speed >= 0 && speed <= 4) {
            engineThrust = speed;

            switch (engineThrust) {
                case 0:
                    this.milesPerDay = 532600;
                    engineSpeed = "Off";
                    break;
                case 1:
                    this.milesPerDay = 1446480;
                    engineSpeed = "Low";
                    break;
                case 2:
                    this.milesPerDay = 2892960;
                    engineSpeed = "Medium";
                    break;
                case 3:
                    this.milesPerDay = 4340000;
                    engineSpeed = "High";
                    break;
                case 4:
                    this.milesPerDay = 6874000;
                    engineSpeed = "Turbo";
                    break;
                default:
                    break;
            }
            updatePowerUsage();
        } else {
            System.out.println("Unexpected option encountered, speed remains unchanged.");
        }
    }
    // used to update the stats after an event
    public void updateCreditsStat(int creditsChange) {
        if (creditsChange > 0) {
            this.credits += creditsChange;
            System.out.println("+" + creditsChange + " Credits");
        } else if (creditsChange < 0) {
            if (this.credits+creditsChange <= 0) {
                System.out.println("Your wallet was bled dry. They took every last Credit you had.");
                this.credits = 0;
            } else {
                this.credits += creditsChange;
            }
            System.out.println(creditsChange + " Credits");
        }
    }
    public void updateOxygenStat(int oxygenChange) {
        if (oxygenChange > 0) {
            System.out.println("+" + oxygenChange + " Oxygen");
            this.oxygen += oxygenChange;
        } else if (oxygenChange < 0) {
            if (this.oxygen+oxygenChange <= 0) {
                System.out.println("That's nearly all of your air! You quickly isolate all the oxygen to the living quarters to eek out a few extra breaths worth.");
                this.oxygen = 5;
            } else {
                this.oxygen += oxygenChange;
            }
            System.out.println(oxygenChange + " Oxygen");
        }
    }
    public void updatePowerStat(int powerChange) {
        if (powerChange > 0) {
            System.out.println("+" + powerChange + " Power");
            this.power += powerChange;
        } else if (powerChange < 0) {
            if (this.power+powerChange <= 0){
                this.power = 0;
                setEngineSpeed(0);
                System.out.println("Your ship has run out of power! The engines have shut off in hopes of recharging with the solar panels.");
            } else {
                this.power += powerChange;
            }
            System.out.println(powerChange + " Power");
        }
    }
    public void updateRationsStat(int rationsChange) {
        if (rationsChange > 0) {
            System.out.println("+" + rationsChange + " Rations");
            this.rations += rationsChange;
        } else if (rationsChange < 0) {
            if (this.rations+rationsChange <= 0) {
                System.out.println("That was your very last morsel of food. You'll soon starve if you don't find something to eat!");
                if (daysWithoutFood == 0) {
                    daysWithoutFood = 1;
                }
            } else {
                this.rations += rationsChange;
            }
            System.out.println(rationsChange + " Rations");
        }
    }

    // public void setOxygenConsumption(int extraO2Usage) {
    //     // TODO change this when damaged or more crew members
    //     this.oxygenConsumption = 1 + extraO2Usage;
    // }

    public void setMealPlan(int dietNumber) {
        switch (dietNumber) {
            case 1:
                rationUsage = 0;
                mealPlan = "Meager";
                break;
            case 2:
                rationUsage = 2;
                mealPlan = "Lean";
                break;
            case 3:
                rationUsage = 4;
                mealPlan = "Regular";
                break;
            default:
            System.err.println("NUMBER NOT RECOGNIZED FOR RATION CHANGE");
                break;
        }
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
        // // add shield power draw
        // if (this.shieldsOn) {
        //     defaultPowerDraw++;
        // }
        // add engine power draw (0*2 is still 0)
        defaultPowerDraw += calcEngingPowerDraw(engineThrust);
        this.powerUsage = defaultPowerDraw;
    }

    // SHOP ITEMS BELOW
    public int buyItems(String item, int qty, int unitPrice) {
        String shopResponse = "";

        if ((qty * unitPrice) > this.credits) {
            System.out.println("\nWho are you trying to fool? You don't have that kind of money!\n");
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
                if (canBuyShield == false) {
                    System.out.println("\nYou've already upgraded your shields. They're already top of the line.\n");
                    return 0;
                }
                canBuyShield = false;
                shopResponse = "I activated the \"Rocks and Raiders\" package on your shields. ";
            } else {
                shopResponse = "Hope you're a good pilot! ";
                return 0;
            }
        } else if (qty == 0) {
            System.out.println("\nOkay, no problem. Your money's probably no good anyway...\n");
            return 1;
        } else if (qty > 0) {
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
        } else {
            // SELLING ITEMS
            switch (item) {
                case "power":
                    if (Math.abs(qty) <= powerCapacity) {
                        powerCapacity += qty;
                        // if selling the batteries makes the total charge greater than the capacity, fix it
                        if (power > powerCapacity) {
                            power = powerCapacity;
                        }
                        shopResponse = (Math.abs(qty*unitPrice) + " Credits for those power cells of yours. ");
                    } else {
                        System.out.println("\nAre you kidding me?? You ain't got that many batteries! No deal!\n");
                        return -1;
                    }
                    break;
                case "rations":
                    if (Math.abs(qty) <= rations) {
                        rations += qty;
                        shopResponse = (Math.abs(qty*unitPrice) + " Credits for your rations. Hope you don't starve out there. ");
                    } else {
                        System.out.println("\nDo you think I'm stupid?? I know how to count, and you ain't got that much food on you!\n");
                        return -1;
                    }
                    break;
                default:
                    System.err.println("Unknown shop item! Aborting purchase");
                    return -1;
            }
        }

        this.credits -= (qty * unitPrice);
        System.out.println("\nHere ya go, " + shopResponse + "Thanks for your business!\n");
        // System.out.println("Credits remaining:" + credits + "\n");
        return 0;
    }

    // parse the stats from the currently running event
    public void parseEvent(RandomEvent event) {
        int creditsChange = event.getCreditsChange();
        int oxygenChange = event.getOxygenChange();
        int powerChange = event.getPowerChange();
        int rationsChange = event.getRationsChange();

        System.out.println(event.getEventName() + "\n");
        System.out.println(event.getEventDescription() + "\n\nRESULTS");

        updateCreditsStat(creditsChange);
        updateOxygenStat(oxygenChange);
        updatePowerStat(powerChange);
        updateRationsStat(rationsChange);
    }

    // updates status for power, oxygen and rations when called
    public int updateStatus() {
        int warningCount = 0;

        Random randomVal = new Random();

        // if the diet is poor roll for ailment
        if (rationUsage < 4) {
            if (randomVal.nextInt(10) > 8){
                System.out.println("You are too tired to focus properly. You are unable to pilot the ship at a faster speed right now. It's just painful to exist.\n");
                setEngineSpeed(1);
            }
        }

        // clamp power value
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

        // clamp oxygen value
        oxygen = Math.clamp((oxygen - oxygenConsumption), 0, 100);
        // check for 0
        if (oxygen == 10) {
            System.out.println("WARNING: OXYGEN REMAINING IS BELOW 10%. PLEASE SEEK A RESUPPLY DEPOT IMMEDIATELY\n");
            warningCount++;
        }
        if (oxygen < 10) {
            System.out.println("WARNING: OXYGEN REMAINING IS BELOW 10%. PLEASE SEEK A RESUPPLY DEPOT IMMEDIATELY\n");
        }
        if (oxygen == 0) { 
            System.out.print("\033\143");
            // Hitchhikers Guide quote
            System.out.println("Well, that's no good. You're out of oxygen!\n\nIn your last moments you remember something- a popular traveler's guide. It stated that \"if you hold a lungful of air you can survive in the total vacuum of space for about thirty seconds. However, it does go on to say that what with space being the mindboggling size it is the chances of getting picked up by another ship within those thirty seconds are two to the power of two hundred and seventy-six thousand seven hundred and nine to one against\". You did not beat those odds.\n\nGAME OVER\n\n");
            System.exit(0);
        }

        // clamp rations value
        if (rations >= 0) {
            rations = Math.clamp((rations - rationUsage), 0, 99999);
            if (rations == 0) {
                // TODO allow for 14 more days to pass, increase illness
                rations = 0;
                System.out.println("Your stomach is in pain, and you're getting weak. You're not sure how long you can really survive without food!\n");
                System.out.println("You are too tired to focus properly. You are unable to pilot the ship at a faster speed right now. It's just painful to exist.\n");
                setEngineSpeed(1);
                
                // only set on the first day, no need to annoy the player
                if (daysWithoutFood == 0) {
                    warningCount++;
                }
                daysWithoutFood++;

                // TODO maybe randomize this between 14-31 the first time rations goes to 0 ?
                if (daysWithoutFood >= 14) {
                    System.out.print("\033\143");
                    System.out.println("You've held out for as long as you could, but your strength has failed you. You can't think, you can't speak, you can't move from where you lie on "+ shipName +"\'s cold floor.\n\nGAME OVER");
                    System.exit(0);
                }
            }
        }
        return warningCount;
    }
}
