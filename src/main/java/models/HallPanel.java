package models;

import models.enums.RequestType;

public class HallPanel {
    private int floorNumber;
    private ElevatorController elevatorController;

    

    public HallPanel(int floorNumber, ElevatorController elevatorController) {
        this.floorNumber = floorNumber;
        this.elevatorController = elevatorController;
    }

    public void pressUp() {
        

        elevatorController.requestElevator(floorNumber, RequestType.PICKUP_UP);
    }

    public void pressDown() {
        

        elevatorController.requestElevator(floorNumber, RequestType.PICKUP_DOWN);
    }
}
