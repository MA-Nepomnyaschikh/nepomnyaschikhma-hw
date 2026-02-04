package express_java.practice_8.part_2.task_5;

import java.util.Objects;

public class Item {

    private int ItemId;
    private String ItemName;

    public Item(int itemId, String itemName) {
        ItemId = itemId;
        ItemName = itemName;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return ItemId == item.ItemId && Objects.equals(ItemName, item.ItemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ItemId, ItemName);
    }

    @Override
    public String toString() {
        return "{" + ItemId + ", " + ItemName + "}";
    }
}
