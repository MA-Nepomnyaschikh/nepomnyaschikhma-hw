package practice_12.task_5;

import practice_12.task_5.exceptions.OutOfStockException;
import practice_12.task_5.validators.ProductValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class InventoryService {

    private static boolean isInventoryOpen = true;

    ConcurrentHashMap<String, ConcurrentLinkedQueue<Product>> inventory = new ConcurrentHashMap<>();

    public void addProduct(Product product) {
        checkInventory("Инвентарь закрыт. Продукт не был добавлен");

        ProductValidator.validateProduct(product);

        inventory.computeIfAbsent(product.getCategory(), k -> new ConcurrentLinkedQueue<>())
                .add(product);
    }

    public Product getProductFormCategory(String category) {
        checkInventory("Инвентарь закрыт. Продукт не получить");

        Queue<Product> products = requireCategoryExist(category);
        Product product = products.poll();
        if (product == null) {
            throw new OutOfStockException("В указанной категории отсутствуют товары");
        }
        return product;
    }

    public List<Product> getProductsByPrice(double minPrice, double maxPrice) {
        checkInventory("Инвентарь закрыт. Продукты не получить");

        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Минимальная цена не может быть больше максимальной");
        }

        return inventory.values().stream()
                .flatMap(value -> value.stream())
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Product> getProductsFromCategoryByPrice(String category, double minPrice, double maxPrice) {
        checkInventory("Инвентарь закрыт. Продукты не получить");

        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Минимальная цена не может быть больше максимальной");
        }

        Queue<Product> products = requireCategoryExist(category);
        if (products.isEmpty()) {
            throw new OutOfStockException("В указанной категории отсутствуют товары");
        }

        return products.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(String category) {
        checkInventory("Инвентарь закрыт. Продукты не получить");

        Queue<Product> products = requireCategoryExist(category);
        return new ArrayList<>(products);
    }

    public void addCategory(String category) {
        ProductValidator.validateCategory(category);

        if (inventory.putIfAbsent(category, new ConcurrentLinkedQueue<>()) != null) {
            throw new IllegalArgumentException("Указанная категория товаров уже зарегистрирована на складе");
        }
    }

    public static boolean isInventoryOpen() {
        return isInventoryOpen;
    }

    public static void setIsInventoryOpen(boolean isInventoryOpen) {
        InventoryService.isInventoryOpen = isInventoryOpen;
    }

    private void checkInventory(String message) {
        if (!isInventoryOpen) {
            throw new IllegalStateException(message);
        }
    }

    public int size() {
        return inventory.size();
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    private Queue<Product> requireCategoryExist(String category) {
        ProductValidator.validateCategory(category);

        Queue<Product> products = inventory.get(category);
        if (products == null) {
            throw new OutOfStockException("Указанная категория товаров не зарегистрирована на складе");
        }
        return products;
    }
}
