package models;


import java.util.List;

import models.enums.RequestType;
import strategies.ElevatorSelectionStrategy;

public class ElevatorController {

    

    List<Elevator> elevators;
    ElevatorSelectionStrategy elevatorSelectionStrategy;
    // private final Object lock = new Object();

    

    public ElevatorController(
            List<Elevator> elevators,
            ElevatorSelectionStrategy elevatorSelectionStrategy) {

        this.elevators = elevators;
        this.elevatorSelectionStrategy = elevatorSelectionStrategy;

        // // Every elevator shares the controller's lock.
        //for (Elevator elevator : elevators) {
         //   elevator.setLock(lock);
        //}
    }

   //handles hall requests and selects an elevator
    public Elevator requestElevator(int floorNumber, RequestType type){
        //synchronized (lock) {
         if (type == RequestType.DESTINATION){
            return null;  // hall calls only since we dont need to select elevator when call made from inside elevator
         }
         // Create Request at the boundary, pass it through the system
        Request request = new Request(floorNumber, type);
        Elevator best = elevatorSelectionStrategy.selectBestElevator(
                        elevators,
                        request
                       );


        best.addRequest(request);

        return best;
        //}
        
    }

    public void step() {
        //synchronized (lock) {
            for (Elevator elevator : elevators) {
                elevator.step();
            }
       //}
    }

    public void enableEnergyEfficientMode(Elevator elevator){
        elevator.enableEnergyEfficientMode();
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public ElevatorSelectionStrategy getElevatorSelectionStrategy() {
        return elevatorSelectionStrategy;
    }
    


    

    
    
}
