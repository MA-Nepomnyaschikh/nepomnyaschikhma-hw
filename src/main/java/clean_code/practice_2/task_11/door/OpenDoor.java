package clean_code.practice_2.task_11.door;

import clean_code.practice_2.task_11.Operation;

public class OpenDoor implements Operation {

    private final Door door;

    public OpenDoor(Door door) {
        this.door = door;
    }

    public void execute() {
        if (door.isOpen()) {
            throw new IllegalStateException("Дверь уже открыта");
        }
        if (door.isLocked()) {
            throw new IllegalStateException("Дверь заблокирована");
        }
        door.setOpen(true);
        System.out.println("Дверь открыта");
    }
}
