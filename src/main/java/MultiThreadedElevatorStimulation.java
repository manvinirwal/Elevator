import models.ElevatorController;
import models.Request;
import models.enums.RequestType;

public class MultiThreadedElevatorStimulation {
    



    public static void multithreadedStimulation(ElevatorController controller) throws InterruptedException {

       

        // Thread 1: requests from one floor
        Thread user1 = new Thread(() -> {
            controller.requestElevator(2, RequestType.PICKUP_UP);
        });

        // Thread 2: requests from another floor
        Thread user2 = new Thread(() -> {
            controller.requestElevator(7, RequestType.PICKUP_DOWN);
        });

        // Thread 3: destination requests
        Thread user3 = new Thread(() -> {
            controller.getElevators().get(0).addRequest(new Request(5, RequestType.DESTINATION));
        });

        // Thread 4: elevator movement simulation
        Thread movementThread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                controller.step();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        user1.start();
        user2.start();
        user3.start();
        movementThread.start();

        user1.join();
        user2.join();
        user3.join();
        movementThread.join();

        System.out.println("Simulation completed");
    }

 

    
}
