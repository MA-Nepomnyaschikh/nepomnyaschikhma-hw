package clean_code.practice_2.task_11.door;

public class Door {

    private boolean open;
    private boolean locked;

    public boolean isOpen() {
        return open;
    }

    void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isLocked() {
        return locked;
    }

    void setLock(boolean lock) {
        this.locked = lock;
    }
}
