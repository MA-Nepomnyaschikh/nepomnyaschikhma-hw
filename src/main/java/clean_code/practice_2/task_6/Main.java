package clean_code.practice_2.task_6;

public class Main {
    public static void main(String[] args) {
        GraphicElementsFactory factory = new WindowsElementsFactory();

        factory.createButton().click();
        factory.createMenu().open();
        factory.createCheckbox().check();

        System.out.println();

        factory = new MacElementsFactory();

        factory.createButton().click();
        factory.createMenu().open();
        factory.createCheckbox().check();
    }
}
