package clean_code.practice_2.task_6;

public class WindowsElementsFactory extends GraphicElementsFactory {
    @Override
    public Button createButton() {
        System.out.println("Кнопка для системы Windows отрисована");
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        System.out.println("Чекбокс для системы Windows отрисован");
        return new WindowsCheckbox();
    }

    @Override
    public Menu createMenu() {
        System.out.println("Меню для системы Windows отрисовано");
        return new WindowsMenu();
    }
}
