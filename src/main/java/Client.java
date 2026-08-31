

import java.util.Arrays;

import models.Elevator;
import models.ElevatorController;
import models.enums.RequestType;
import observers.DisplayPanel;
import models.enums.Direction;
import models.enums.ElevatorState;
import strategies.DirectionAwareElevatorSelectionStrategy;
import strategies.ElevatorSelectionStrategy;
import strategies.MovementStrategy;
import strategies.ScanMovementStrategy;

public class Client {

    public static void main(String[] args) {

        /*
         * Create movement strategy.
         *
         * Elevator delegates its movement logic to this strategy.
         */
        MovementStrategy movementStrategy =
                new ScanMovementStrategy();

        /*
         * Create elevators.
         */
        Elevator elevator1 = new Elevator(
                1,
                100,                 // capacity
                0,                   // starting floor
                Direction.IDLE,
                ElevatorState.OPERATING,
                movementStrategy
        );
        DisplayPanel dp1 = new DisplayPanel(elevator1);

        Elevator elevator2 = new Elevator(
                2,
                100,
                5,
                Direction.IDLE,
                ElevatorState.OPERATING,
                movementStrategy
        );
        DisplayPanel dp2 = new DisplayPanel(elevator2);

        /*
         * ElevatorController decides which elevator
         * should handle a hall request.
         */
        ElevatorSelectionStrategy selectionStrategy =
                new DirectionAwareElevatorSelectionStrategy();

        ElevatorController controller =
                new ElevatorController(
                        Arrays.asList(elevator1, elevator2),
                        selectionStrategy
                );


        // ---------------------------------------------------------
        // SCENARIO 1:
        // Passenger at floor 3 wants to go UP.
        // ---------------------------------------------------------

        System.out.println("Passenger presses UP at floor 3");

        Elevator selected =
                controller.requestElevator(
                        3,
                        RequestType.PICKUP_UP
                );

        System.out.println(
                "Elevator selected at floor "
                        + selected.getCurrentFloor()
        );


        // ---------------------------------------------------------
        // Simulate elevator ticks.
        // ---------------------------------------------------------

        System.out.println("\n--- Starting elevator movement ---");

        for (int i = 0; i < 5; i++) {

            System.out.println(
                    "Before tick: floor="
                            + selected.getCurrentFloor()
                            + ", direction="
                            + selected.getCurrentDirection()
                            + ", doorOpen="
                            + selected.getDoor().isOpen()
            );

            controller.step();

            System.out.println(
                    "After tick:  floor="
                            + selected.getCurrentFloor()
                            + ", direction="
                            + selected.getCurrentDirection()
                            + ", doorOpen="
                            + selected.getDoor().isOpen()
            );
        }


        // ---------------------------------------------------------
        // Passenger enters elevator.
        // Passenger wants to go to floor 7.
        // ---------------------------------------------------------

        System.out.println(
                "\nPassenger enters elevator and presses floor 7"
        );

        selected
                .getElevatorPanel()
                .pressFloor(7);


        // ---------------------------------------------------------
        // Continue simulation.
        // ---------------------------------------------------------

        System.out.println("\n--- Moving to floor 7 ---");

        for (int i = 0; i < 6; i++) {

            System.out.println(
                    "Before tick: floor="
                            + selected.getCurrentFloor()
                            + ", direction="
                            + selected.getCurrentDirection()
                            + ", doorOpen="
                            + selected.getDoor().isOpen()
            );

            controller.step();

            System.out.println(
                    "After tick:  floor="
                            + selected.getCurrentFloor()
                            + ", direction="
                            + selected.getCurrentDirection()
                            + ", doorOpen="
                            + selected.getDoor().isOpen()
            );
        }


        // ---------------------------------------------------------
        // SCENARIO 2:
        // Passenger presses emergency stop.
        // ---------------------------------------------------------

        System.out.println("\nPassenger presses EMERGENCY STOP");

        selected
                .getElevatorPanel()
                .pressEmergencyStop();

        System.out.println(
                "Elevator state = "
                        + selected.getState()
        );


        // ---------------------------------------------------------
        // step() should now do nothing because elevator
        // is in EMERGENCY_STOP state.
        // ---------------------------------------------------------

        System.out.println("\nCalling controller.step()");

        controller.step();

        System.out.println(
                "Elevator floor after emergency = "
                        + selected.getCurrentFloor()
        );
    }
}