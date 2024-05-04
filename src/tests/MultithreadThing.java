package tests;

public class MultithreadThing implements Runnable{
    
    int counter = 0;

    @Override
    public void run() {
        while (counter > 100)
        System.out.println("This is a test - Loop: " + counter);
        counter++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

    }
}
