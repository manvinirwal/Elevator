package models;

import java.util.ArrayList;
import java.util.List;

public class Building {

   private  List<Floor> floors;
   private ElevatorController elevatorController;

   public Building(int numberOfFloors,
                    ElevatorController elevatorController) {

        this.elevatorController = elevatorController;
        this.floors = new ArrayList<>();

        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i, elevatorController));
        }
    }
   // Getters and Setters  

   public List<Floor> getFloors() {
    return floors;
   }

   public void setFloors(List<Floor> floors) {
    this.floors = floors;
   }

   public ElevatorController getElevatorController() {
    return elevatorController;
   }

   public void setElevatorController(ElevatorController elevatorController) {
    this.elevatorController = elevatorController;
   }

   public void addFloor(Floor floor) {
       floors.add(floor);
   }
   public void removeFloor(Floor floor) {
       floors.remove(floor);
   }
  
}
