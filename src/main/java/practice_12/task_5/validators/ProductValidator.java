package practice_12.task_5.validators;

import practice_12.task_5.Product;

public class ProductValidator {

    public static void validateProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Продукт не может быть null");
        if (product.getName() == null || product.getName().isBlank()) throw new IllegalArgumentException("Имя продукта не может быть null или пустым");
        if (product.getPrice() < 0) throw new IllegalArgumentException("Цена продукта не может быть отрицательной");
        validateCategory(product.getCategory());
    }

    public static void validateCategory(String category) {
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Категория продукта не может быть null или пустой");
    }
}
