package strategies;

import java.util.List;

import models.Elevator;
import models.Request;
import models.enums.Direction;
import models.enums.RequestType;

public class DirectionAwareElevatorSelectionStrategy implements ElevatorSelectionStrategy{

    @Override
    public Elevator selectBestElevator(List<Elevator> elevators, Request request) {

        // priority 1 : Nearest elevator  moving towards the request floor in the right direction and has not surpassed the request floor
       
        Elevator best = findMovingTowards(elevators, request);
        if(best != null){
            return best;
        }

        // priority 2 : pick nearest idle
        best = findNearestIdle(elevators, request.getFloor());

        if(best != null){
            return best;
        }

        // priority 3 : pick nearest

        return findNearest(elevators, request.getFloor());
       
    }

    private Elevator findMovingTowards(List<Elevator> elevators, Request request){
        int requestFloor = request.getFloor();
        Elevator nearest = null;
        Direction requestDirection = request.getRequestType()  == RequestType.PICKUP_UP ? Direction.UP  : Direction.DOWN;
        int nearestDist = Integer.MAX_VALUE;

        for(Elevator e : elevators){

            if(e.getCurrentDirection() != requestDirection){
                continue;
            }

            // elevator has already surpassed the request floor

            if((e.getCurrentDirection() == Direction.UP  && e.getCurrentFloor()  >  requestFloor) ||
               (e.getCurrentDirection() == Direction.DOWN  && e.getCurrentFloor() < requestFloor) ){

                continue;

            }


                        // This check is necessary in a multithreaded implementation where
            // ElevatorController.requestElevator() and Elevator.step() may execute
            // concurrently.
            //
            // There is no guarantee that the new request is added to the elevator's
            // request queue before the next step() executes.
            //
            // Example:
            // Elevator is at floor 3, moving UP.
            // Existing queue = {floor 4}.
            //
            // A new request arrives at floor 7 (PICKUP_UP).
            //
            // Thread 1: Elevator.step()
            // Thread 2: requestElevator(7, PICKUP_UP)
            //
            // If step() executes first:
            //   - Elevator moves to floor 4.
            //   - It serves floor 4.
            //   - No requests are left in the queue.
            //   - Elevator may reverse direction / become IDLE.
            //
            // Only after that, Thread 2 adds the floor 7 request.
            //
            // So although the elevator was at floor 3 and moving UP when we
            // received the request, it was NOT actually committed to reaching
            // floor 7.
            //
            // Therefore, when selecting an elevator for a new request, we should
            // consider an elevator moving in the correct direction only if its
            // existing queue already contains a request at or beyond the new
            // request floor.
            //
            // Example:
            //   Elevator at floor 3, moving UP
            //   Existing request = floor 4
            //   New request      = floor 7
            //
            //   Since the existing queue does not go beyond floor 7,
            //   this elevator is not considered "committed" to reaching floor 7.
            //
            // Example:
            //   Elevator at floor 3, moving UP
            //   Existing request = floor 8
            //   New request      = floor 7
            //
            //   The elevator is already committed to reaching floor 8,
            //   so it will naturally pass floor 7 while moving UP.
            //   Therefore, it is a good candidate for the new request.
            //
            // Example:
            //   Elevator at floor 3, moving UP
            //   Existing request = floor 7
            //   New request      = floor 7
            //
            //   The elevator is already committed to reaching floor 7,
            //   so it is also a valid candidate.
            //
            // This check is mainly important when request arrival and elevator
            // movement can happen concurrently. In a single-threaded/tick-based
            // implementation where request addition is guaranteed to happen
            // before step(), this check is not strictly necessary.


            //if (! e.hasRequestsAtOrBeyond(requestFloor, requestDirection)) {
               // continue;
            // }

            // above check is not necessary for LLD interviews

            int dist = Math.abs(e.getCurrentFloor() - requestFloor);

            if(dist < nearestDist){
                nearest = e;
                nearestDist = dist;
            }

            



        }
        return nearest;

    }

     private Elevator findNearestIdle(List<Elevator> elevators, int requestFloor){

        Elevator nearest = null;
        int nearestDist = Integer.MAX_VALUE;

       for(Elevator e : elevators){

        if(e.getCurrentDirection() != Direction.IDLE){
            continue;
        }
         
         int dist = Math.abs(e.getCurrentFloor() - requestFloor);

         if(dist < nearestDist){
             nearest = e;
             nearestDist = dist;
         }
        
         

       }
          return nearest;

    }

    private Elevator findNearest(List<Elevator> elevators, int requestFloor){

        Elevator nearest = null;
        int nearestDist = Integer.MAX_VALUE;

       for(Elevator e : elevators){
         
         int dist = Math.abs(e.getCurrentFloor() - requestFloor);

         if(dist < nearestDist){
             nearest = e;
             nearestDist = dist;
         }
        
         

       }
          return nearest;

    }




    
}
