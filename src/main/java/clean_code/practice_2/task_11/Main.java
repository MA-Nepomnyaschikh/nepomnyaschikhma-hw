package clean_code.practice_2.task_11;

import clean_code.practice_2.task_11.door.Door;

public class Main {
    public static void main(String[] args) {
        Door door = new Door();
        DoorFacade doorFacade = new DoorFacade(door);

        doorFacade.openDoor();
        doorFacade.closeDoor();
        doorFacade.lockDoor();
    }
}
