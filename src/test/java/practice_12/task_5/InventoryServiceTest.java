package practice_12.task_5;

import org.junit.jupiter.api.BeforeEach;

public class InventoryServiceTest {

    protected InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        inventoryService = new InventoryService();
        InventoryService.setIsInventoryOpen(true);
    }
}
