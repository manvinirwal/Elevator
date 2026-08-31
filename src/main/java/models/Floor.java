package models;
public class Floor {
    int floorNumber;
    HallPanel hallPanel;

    public Floor(int floorNumber, ElevatorController ec){
        this.floorNumber = floorNumber;
        this.hallPanel = new HallPanel(floorNumber, ec);

    }
    
}
