package clean_code.practice_2.task_11;

import clean_code.practice_2.task_11.door.CloseDoor;
import clean_code.practice_2.task_11.door.Door;
import clean_code.practice_2.task_11.door.LockDoor;
import clean_code.practice_2.task_11.door.OpenDoor;

public class DoorFacade {
    private final OpenDoor openDoor;
    private final CloseDoor closeDoor;
    private final LockDoor lockDoor;

    public DoorFacade(Door door) {
        this.openDoor = new OpenDoor(door);
        this.closeDoor = new CloseDoor(door);
        this.lockDoor = new LockDoor(door);
    }

    public void openDoor() {
        openDoor.execute();
    }

    public void closeDoor() {
        closeDoor.execute();
    }

    public void lockDoor() {
        lockDoor.execute();
    }
}
