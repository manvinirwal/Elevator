package models;

public class Door {
    private boolean isOpen;

    public Door() {
        this.isOpen = false;
    }

    public void open() {
        isOpen = true;
        System.out.println("Door is now open.");
    }

    public void close() {
        isOpen = false;
        System.out.println("Door is now closed.");
    }

    public boolean isOpen() {
        return isOpen;
    }
}


