package practice_12.task_5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import practice_12.task_5.exceptions.OutOfStockException;

public class GetProductFormCategoryTest extends InventoryServiceTest {

    @Test
    void getProductFormCategoryTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);

        Product actualProduct = inventoryService.getProductFormCategory("Садовый инвентарь");
        Assertions.assertEquals(product, actualProduct);
    }

    @Test
    void getProductFormInvalidCategoryTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);

        Assertions.assertThrows(IllegalArgumentException.class, () -> inventoryService.getProductFormCategory(null));
    }

    @Test
    void getProductFormUndefinedCategoryTest() {
        Assertions.assertThrows(OutOfStockException.class, () -> inventoryService.getProductFormCategory("Садовый инвентарь"));
    }

    @Test
    void getProductFormEmptyCategoryTest() {
        inventoryService.addCategory("Садовый инвентарь");

        Assertions.assertThrows(OutOfStockException.class, () -> inventoryService.getProductFormCategory("Садовый инвентарь"));
    }

    @Test
    void getProductFormCategoryWithDisabledFlagTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);
        InventoryService.setIsInventoryOpen(false);

        Assertions.assertThrows(IllegalStateException.class, () -> inventoryService.getProductFormCategory("Садовый инвентарь"));
    }




}
