package observers;

import models.Elevator;

public class DisplayPanel implements ElevatorObserver{
    

    public DisplayPanel(Elevator elevator) {
        

        // Register itself as an observer of the elevator.
        elevator.addObserver(this);
    }
    @Override
    public void update(Elevator elevator) {

        System.out.println(
                  "ElevatorId: " + elevator.getId()
                + " | Floor: " + elevator.getCurrentFloor()
                + " | Direction: " + elevator.getCurrentDirection()
                + " | State: " + elevator.getState()
        );
    }
    
}
