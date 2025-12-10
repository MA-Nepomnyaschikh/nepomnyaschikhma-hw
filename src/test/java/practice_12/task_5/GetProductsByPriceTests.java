package practice_12.task_5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetProductsByPriceTests extends InventoryServiceTest {
    @Test
    public void getProductsByPriceTest() {
        List<Product> products = List.of(
                new Product("Лопата", 500, "Садовый инвентарь"),
                new Product("Вилы", 200, "Садовый инвентарь"),
                new Product("Тяпка", 400, "Садовый инвентарь"));

        for (Product product : products) {
            inventoryService.addProduct(product);
        }

        List<Product> actualResult = inventoryService.getProductsByPrice(300, 500);
        Assertions.assertEquals(2, actualResult.size());
        Assertions.assertTrue(actualResult.contains(products.get(0)));
        Assertions.assertTrue(actualResult.contains(products.get(2)));
        Assertions.assertFalse(actualResult.contains(products.get(1)));
    }

    @Test
    public void getProductsByPriceFromEmptyCategoriesTest() {
        inventoryService.addCategory("Садовый инвентарь");

        List<Product> actualResult = inventoryService.getProductsByPrice(300, 500);
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    public void getProductsByPriceWithInvalidInputTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> inventoryService.getProductsByPrice(600, 500));
    }

    @Test
    public void getProductsByPriceWithDisabledFlagTest() {
        InventoryService.setIsInventoryOpen(false);
        Assertions.assertThrows(IllegalStateException.class, () -> inventoryService.getProductsByPrice(600, 500));
    }
}
