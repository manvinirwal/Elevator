package strategies;

import java.util.List;

import models.Elevator;
import models.Request;

public interface ElevatorSelectionStrategy {

    public Elevator selectBestElevator( List<Elevator> elevators,Request request);

    
}
