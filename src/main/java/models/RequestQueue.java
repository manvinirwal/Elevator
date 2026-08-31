package models;



import java.util.HashSet;
import java.util.Set;

import models.enums.Direction;
import models.enums.RequestType;

public class RequestQueue {

    /*
     * Set is used to prevent duplicate requests.
     *
     * Two requests are considered duplicates when they have
     * the same floor and the same RequestType.
     *
     * Example:
     * Request(5, PICKUP_UP)    -> duplicate of another
     * Request(5, PICKUP_UP)
     *
     * But these are different requests:
     * Request(5, PICKUP_UP)
     * Request(5, PICKUP_DOWN)
     */
    private final Set<Request> requests;

    public RequestQueue() {
        this.requests = new HashSet<>();
    }

    /*
     * Adds a request to the queue.
     *
     * Since requests is a Set, adding an already existing request
     * has no effect.
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /*
     * Removes a request after the elevator has served it.
     */
    public void removeRequest(Request request) {
        requests.remove(request);
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }

    /*
     * Checks whether there is any request ahead of the elevator
     * in the given direction.
     *
     * Important:
     * We only check the floor here, not the RequestType.
     *
     * Example:
     *
     * Elevator is at floor 3 and moving UP.
     * Request: floor 5, PICKUP_DOWN
     *
     * There is still a request ahead of the elevator, so this
     * method returns true.
     *
     * The elevator may travel towards floor 5, but it should
     * NOT stop there while moving UP because the passenger
     * wants to travel DOWN.
     *
     * Therefore, "should I continue travelling?" and
     * "should I stop at this floor?" are separate questions.
     */
    public boolean hasRequestsAhead(
            int currentFloor,
            Direction direction) {

        for (Request request : requests) {

            if (direction == Direction.UP
                    && request.getFloor() > currentFloor) {
                return true;
            }

            if (direction == Direction.DOWN
                    && request.getFloor() < currentFloor) {
                return true;
            }
        }

        return false;
    }

    /*
     * Returns a request that should be served at the current floor.
     *
     * A DESTINATION request is always served because the passenger
     * inside the elevator has explicitly selected this floor.
     *
     * A pickup request is served only when the elevator is moving
     * in the direction requested by the passenger or if elevtaor is idle and at the current floor.
     *
     * Example:
     *
     * Elevator at floor 5, moving UP:
     *
     * PICKUP_UP   -> stop
     * PICKUP_DOWN -> do not stop
     * DESTINATION -> stop
     * 
     * 
     * 
     * Elevator at floor 5, Idle
     * PICKUP_UP , PICKUP_DOWN, DESTINATION -- stop
     */

    public Request getStopRequestIfAny(
        int currentFloor,
        Direction direction) {

        // A destination request is always served,
        // irrespective of the current direction.
        Request destinationRequest =
                new Request(currentFloor, RequestType.DESTINATION);

        if (requests.contains(destinationRequest)) {
            return destinationRequest;
        }

        // If elevator is idle, serve either type of hall request
        // because the request is already on the current floor.
        if (direction == Direction.IDLE) {

            Request hallUpRequest =
                    new Request(currentFloor, RequestType.PICKUP_UP);

            if (requests.contains(hallUpRequest)) {
                return hallUpRequest;
            }

            Request hallDownRequest =
                    new Request(currentFloor, RequestType.PICKUP_DOWN);

            if (requests.contains(hallDownRequest)) {
                return hallDownRequest;
            }
        }

        // When moving UP, only PICKUP_UP should be served.
        else if (direction == Direction.UP) {

            Request hallUpRequest =
                    new Request(currentFloor, RequestType.PICKUP_UP);

            if (requests.contains(hallUpRequest)) {
                return hallUpRequest;
            }
        }

        // When moving DOWN, only PICKUP_DOWN should be served.
        else if (direction == Direction.DOWN) {

            Request hallDownRequest =
                    new Request(currentFloor, RequestType.PICKUP_DOWN);

            if (requests.contains(hallDownRequest)) {
                return hallDownRequest;
            }
        }

        return null;
    }

    /*
     * Used when an idle elevator receives a new request.
     *
     * The nearest request is used to determine the initial
     * direction of the elevator.
     */
    public Request getNearestRequest(int currentFloor) {

        Request nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Request request : requests) {

            int distance =
                    Math.abs(request.getFloor() - currentFloor);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = request;
            }
        }

        return nearest;
    }

    /*
     * Checks whether the elevator already has a commitment
     * to travel to or beyond the given floor in the given direction.
     *
     * This is mainly useful for elevator selection when requests
     * can be added concurrently with elevator movement.
     * If step() and requestElevator() can execute concurrently, this can happen:
     *.    Elevator at floor 3
            Direction = UP
            Queue = {floor 4}

            Thread 1                         Thread 2
            --------                         --------
            step()
                                            request floor 7
            see queue has floor 4
            move toward 4
                                            select elevator
                                            add floor 7

            step()
            floor 4
            remove floor 4
            queue temporarily empty
            reverse DOWN
                                            floor 7 request was added,
                                            but elevator has already
                                            decided to reverse
     *
     * Example:
     *
     * Elevator:
     *     currentFloor = 3
     *     direction = UP
     *     queue = { floor 4 }
     *
     * New hall request:
     *     floor = 7
     *     direction = UP
     *
     * Although the elevator is currently moving UP, its existing
     * queue only takes it up to floor 4. It is therefore not
     * committed to reaching floor 7.
     *
     * hasRequestsAtOrBeyond(7, UP) -> false
     *
     * If the queue contains floor 8 instead:
     *
     * hasRequestsAtOrBeyond(7, UP) -> true
     *
     * The elevator is already committed to travelling beyond
     * floor 7, so it is a better candidate for the new request.
     */
    public boolean hasRequestsAtOrBeyond(
            int floor,
            Direction direction) {

        for (Request request : requests) {

            if (direction == Direction.UP
                    && request.getFloor() >= floor) {
                return true;
            }

            if (direction == Direction.DOWN
                    && request.getFloor() <= floor) {
                return true;
            }
        }

        return false;
    }
}
