package express_java.practice_12.task_5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class AddProductTests extends InventoryServiceTest {

    @Test
    public void addProductTest() {
        Product product = new Product("Лопата", 500, "Садовый инвентарь");
        inventoryService.addProduct(product);

        Assertions.assertEquals(1, inventoryService.size());
        Assertions.assertEquals(1, inventoryService.getProductsByCategory("Садовый инвентарь").size());
        Assertions.assertEquals(product, inventoryService.getProductsByCategory("Садовый инвентарь").getFirst());
    }

    @ParameterizedTest
    @MethodSource("invalidProductsProvider")
    public void addInvalidProductTest(Product product) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> inventoryService.addProduct(product));
    }

    private static Stream<Arguments> invalidProductsProvider() {
        return Stream.of(
                Arguments.of(new Product(null, 500, "Садовый инвентарь")),
                Arguments.of(new Product("Лопата", -0.1, "Садовый инвентарь")),
                Arguments.of(new Product("Лопата", 500, null)),
                Arguments.of((Object) null)
        );
    }

    @Test
    public void addProductWithDisabledFlagTest() {
        InventoryService.setIsInventoryOpen(false);
        Product product = new Product("Лопата", 500, "Садовый инвентарь");

        Assertions.assertThrows(IllegalStateException.class, () -> inventoryService.addProduct(product));
    }
}
