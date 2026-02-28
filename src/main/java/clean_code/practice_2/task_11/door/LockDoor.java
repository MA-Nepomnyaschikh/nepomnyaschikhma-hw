package clean_code.practice_2.task_11.door;

import clean_code.practice_2.task_11.Operation;

public class LockDoor implements Operation {

    private final Door door;

    public LockDoor(Door door) {
        this.door = door;
    }

    @Override
    public void execute() {
        if (door.isOpen()) {
            throw new IllegalStateException("Открытую дверь не заблокировать");
        }
        if (door.isLocked()) {
            throw new IllegalStateException("Дверь уже заблокирована");
        }
        door.setLock(true);
        System.out.println("Дверь заблокирована");
    }
}
