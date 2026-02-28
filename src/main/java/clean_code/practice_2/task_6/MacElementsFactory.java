package clean_code.practice_2.task_6;

public class MacElementsFactory extends GraphicElementsFactory {
    @Override
    public Button createButton() {
        System.out.println("Кнопка для системы Mac отрисована");
        return new MacButton();
    }

    @Override
    public Checkbox createCheckbox() {
        System.out.println("Чекбокс для системы Mac отрисован");
        return new MacCheckbox();
    }

    @Override
    public Menu createMenu() {
        System.out.println("Меню для системы Mac отрисовано");
        return new MacMenu();
    }
}
