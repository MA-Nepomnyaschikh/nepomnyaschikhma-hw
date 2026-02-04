package express_java.practice_5.task_5;

public class Main {
    public static void main(String[] args) {
        Farm farm = new Farm();
        Animal chicken = new Chicken();
        Animal cow = new Cow();
        farm.setAnimal(cow);
        farm.feed();
        farm.collectProduce();
    }
}
