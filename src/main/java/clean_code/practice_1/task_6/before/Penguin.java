package clean_code.practice_1.task_6.before;

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Пингвины не летают");
    }
}
