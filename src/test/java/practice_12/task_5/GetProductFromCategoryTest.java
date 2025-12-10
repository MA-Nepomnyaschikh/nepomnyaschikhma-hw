package practice_12.task_5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice_12.task_5.exceptions.OutOfStockException;

public class GetProductFromCategoryTest extends InventoryServiceTest {

    @Test
    void getProductFromCategoryTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);

        Product actualProduct = inventoryService.getProductFromCategory("Садовый инвентарь");
        Assertions.assertEquals(product, actualProduct);
    }

    @Test
    void getProductFromInvalidCategoryTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);

        Assertions.assertThrows(IllegalArgumentException.class, () -> inventoryService.getProductFromCategory(null));
    }

    @Test
    void getProductFromUndefinedCategoryTest() {
        Assertions.assertThrows(OutOfStockException.class, () -> inventoryService.getProductFromCategory("Садовый инвентарь"));
    }

    @Test
    void getProductFromEmptyCategoryTest() {
        inventoryService.addCategory("Садовый инвентарь");

        Assertions.assertThrows(OutOfStockException.class, () -> inventoryService.getProductFromCategory("Садовый инвентарь"));
    }

    @Test
    void getProductFromCategoryWithDisabledFlagTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);
        InventoryService.setIsInventoryOpen(false);

        Assertions.assertThrows(IllegalStateException.class, () -> inventoryService.getProductFromCategory("Садовый инвентарь"));
    }




}
