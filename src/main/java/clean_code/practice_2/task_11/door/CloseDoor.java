package clean_code.practice_2.task_11.door;

import clean_code.practice_2.task_11.Operation;

public class CloseDoor implements Operation {

    private final Door door;

    public CloseDoor(Door door) {
        this.door = door;
    }

    @Override
    public void execute() {
        if (!door.isOpen()) {
            throw new IllegalStateException("Дверь уже закрыта");
        }
        door.setOpen(false);
        System.out.println("Дверь закрыта");
    }
}
