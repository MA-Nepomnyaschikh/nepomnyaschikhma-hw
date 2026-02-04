package clean_code.practice_1.task_7.before;

class Programmer implements Worker {
    @Override
    public void work() {
        System.out.println("Программист пишет код");
    }
    @Override
    public void eat() {
        throw new UnsupportedOperationException("Программист не ест на работе");
    }
}
