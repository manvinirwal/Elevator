package strategies;

import models.Elevator;
import models.Request;
import models.enums.Direction;

public class ScanMovementStrategy implements MovementStrategy {


    // choosing direction form idle and movment can happen in same tick  
    // // similarly reverse direction and movement can happen in same tick
    // so we dont return from step function (after choosing  or reversing direction->we complete our moevemnt)
    // but stop and movment cannot happen in same tick-- so we retrun after stop(ie after catering req at the floor-- cateringreq is  a tick)
  // Choosing direction + moving = same tick
   // Reversing + moving = same tick
    //Stopping(serving req) = one tick
    // Next movement happens in a later tick

    @Override
    public void step(Elevator elevator) {

        /*
         * If there are no pending requests, elevator remains idle.
         */
        if (elevator.getRequestQueue().isEmpty()) {
            elevator.setCurrentDirection(Direction.IDLE);
            return;
        }

        /*
         * If elevator is idle, choose the direction towards
         * the nearest pending request. req is assigned to it  by elevator controller
         */
        if (elevator.getCurrentDirection() == Direction.IDLE) {

            Request nearest =elevator.getRequestQueue().getNearestRequest(elevator.getCurrentFloor());

            if (nearest.getFloor() > elevator.getCurrentFloor()) {

                elevator.setCurrentDirection(Direction.UP);

            } else if (nearest.getFloor() < elevator.getCurrentFloor()) {

                elevator.setCurrentDirection(Direction.DOWN);

            } 
            // If the nearest request is already at the current floor,
            // direction remains IDLE.
            // The request will be served below by stop().
        }

        /*
         * Check whether there is a request at the current floor
         * that should be served.
         *
         * DESTINATION:
         *      Always served.
         *
         * PICKUP_UP:
         *      Served while moving UP or IDLE.
         *
         * PICKUP_DOWN:
         *      Served while moving DOWN or IDLE.
         */
        Request stopRequest =
                elevator.getRequestQueue()
                        .getStopRequestIfAny(
                                elevator.getCurrentFloor(),
                                elevator.getCurrentDirection()
                        );

        if (stopRequest != null) {

            /*
             * Requested floor has been reached.
             */
            System.out.println("Request is currently being served at floor" + elevator.getCurrentFloor());
            elevator.getRequestQueue().removeRequest(stopRequest);

            /*
             * stop of elecator is a separate action/tick.
             */
            elevator.stop();
            return;
            
        }

        /*
         * No request needs to be served at the current floor.
         *
         * Check whether there is any request ahead in the
         * current direction.
         */
        if (!elevator.getRequestQueue().hasRequestsAhead(
                elevator.getCurrentFloor(),
                elevator.getCurrentDirection())) {

            // Reverse direction. Reversal itself is not a tick.
            elevator.setCurrentDirection(
                    elevator.getCurrentDirection() == Direction.UP
                            ? Direction.DOWN
                            : Direction.UP
            );

            /*
            * After reversing, the request at the current floor may now
            * be serviceable.
            *
            * Example:
            *   Elevator at 3, moving DOWN
            *   Request = (3, PICKUP_UP)
            *
            *   No request exists below 3, so we reverse to UP.
            *   Now (3, PICKUP_UP) is serviceable immediately.
            *
            *   We stop at 3 instead of moving 3 -> 4.
            */
            Request stopRequestAfterReverse =
                    elevator.getRequestQueue()
                            .getStopRequestIfAny(
                                    elevator.getCurrentFloor(),
                                    elevator.getCurrentDirection()
                            );

            if (stopRequestAfterReverse != null) {
                System.out.println("Request is currently being served at floor  " + elevator.getCurrentFloor());
                elevator.getRequestQueue()
                        .removeRequest(stopRequestAfterReverse);

                elevator.stop();

                return;
            }
        }

        

        /*
         * Move exactly one floor.
         */
        if (elevator.getCurrentDirection() == Direction.UP) {

            elevator.setCurrentFloor(
                    elevator.getCurrentFloor() + 1
            );

        } else if (elevator.getCurrentDirection() == Direction.DOWN) {

            elevator.setCurrentFloor(
                    elevator.getCurrentFloor() - 1
            );
        }
    }
}