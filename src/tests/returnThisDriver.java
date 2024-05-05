package tests;

public class returnThisDriver {
    public static void main(String[] args) {
        returnThis original = new returnThis();

        System.out.println(original);
        System.out.println(original.getName() +" "+ original.getPower() +" " + original.getRations());
        System.out.println(original.changeall(20, 40, "Mikie"));
        System.out.println("AFTER CHANGE: "+ original.getName() +" "+ original.getPower() +" " + original.getRations());
        
        System.out.println(parseReturnThis(original.changeall(150, 99, "Sammy")));


    }

    public static String parseReturnThis(returnThis object){
        String parseName = "";
        int parsePower = -1;
        int parseRations = -1;

        parseName = object.getName();
        parsePower = object.getPower();
        parseRations = object.getRations();
        return (parseName + " " + parsePower + " " + parseRations);
    }
}
