package models;

import models.enums.RequestType;

public class ElevatorPanel {

    private Elevator elevator;

    public ElevatorPanel(Elevator elevator) {
        this.elevator = elevator;
    }

    /*
     * Passenger presses a floor button.
     *
     * This is always a DESTINATION request because
     * the passenger is already inside the elevator.
     */
    public void pressFloor(int floor) {

        Request request =
                new Request(floor, RequestType.DESTINATION);

        elevator.addRequest(request);
    }

    /*
     * Passenger presses the alarm button.
     */
    public void pressAlarm() {

        elevator.triggerAlarm();
    }

    /*
     * Passenger presses emergency stop.
     */
    public void pressEmergencyStop() {

        elevator.emergencyStop();
    }
    
}