package clean_code.practice_2.task_5;

public class ClassicFurnitureFactory extends FurnitureFactory {
    @Override
    Chair createChair() {
        return new ClassicChair();
    }

    @Override
    Chest createChest() {
        return new ClassicChest();
    }

    @Override
    Table createTable() {
        return new ClassicTable();
    }
}
