package tests;

public class returnThis {
    private int power = 0;
    private int rations = 0;
    private String name = "";

    public returnThis(){
        power = 5;
        rations = 2;
        name = "Alex";
    }

    public returnThis changeall(int power, int rations, String Name) {
        this.power = power;
        this.rations = rations;
        this.name = Name;
        
        return this;
    }

    public String getName() {
        return name;
    }
    public int getPower() {
        return power;
    }
    public int getRations() {
        return rations;
    }
}
